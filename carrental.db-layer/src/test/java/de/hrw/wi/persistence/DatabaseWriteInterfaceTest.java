package de.hrw.wi.persistence;

import de.hrw.wi.databaseSetup.DatabaseConfiguration;
import de.hrw.wi.databaseSetup.InitialDatabaseSetup;
import de.hrw.wi.persistence.dto.BookingDTO;
import de.hrw.wi.types.Datum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Andriessens
 */
public class DatabaseWriteInterfaceTest {
    private static final int TEST_YEAR = 2024;
    private static final String CUSTOMER_ID_00001002 = "00001002";
    private static final String CUSTOMER_ID_00001001 = "00001001";
    private static final String M_LI_200 = "M-LI 200";
    private static final String AUDI = "Audi";
    private static final String RV_TT_777 = "RV-TT 777";

    private static final String DB_URL = DatabaseConfiguration.getDbUrl();
    private static final String USER = DatabaseConfiguration.getDBUser();
    private static final String PASSWORD = DatabaseConfiguration.getDBPassword();

    DatabaseWriteInterface dbWrite;
    DatabaseReadInterface dbRead;

    @BeforeEach
    public void setUp() throws Exception {
        // Show current working directory
        String currentPath = System.getProperty("user.dir");
        System.out.println("Current working directory: " + currentPath);
        // Show database URL
        System.out.println("Database URL: " + DB_URL);

        InitialDatabaseSetup.main(null);
        RealDatabase db = new RealDatabase();
        dbWrite = db;
        dbRead = db;
    }

    @Test
    public void testAddCar() {
        assertEquals(5, dbRead.getAllCars().size());
        dbWrite.addCar(RV_TT_777, AUDI);
        assertEquals(6, dbRead.getAllCars().size());
        assertEquals(AUDI, dbRead.getCarBrand(RV_TT_777));
    }

    @Test
    public void testAddIllegalCar() throws Exception {
        try {
            dbWrite.addCar("", AUDI);
        } catch (PersistenceException e) {
            assertEquals("Car could not be added.", e.getMessage());
            return;
        }
        fail();
    }

    @Test
    public void testIllegalBookCar() {
        // Von- und Bis-Datum für Ausleihe konstruieren
        Datum from = new Datum(2015, 11, 16);
        Datum to = new Datum(2015, 11, 17);

        // Freies Auto suchen
        Set<String> carIds = dbRead.findAvailableCar(from, to);
        String carId = carIds.iterator().next();
        try {
            // Illegale Operation auf Datenbank ausführen: Keine Kunden-ID angegeben
            dbWrite.upsertBookingForCar(carId, "", from, to, DatabaseReadInterface.STATE_OPEN);
        } catch (PersistenceException e) {
            assertEquals("Illegal values, booking could not be updated.", e.getMessage());
            assertTrue(dbRead.isCarAvailable(carId, from, to));
            return;
        }
        fail();
    }

    @Test
    public void testBookCar() {
        Datum from = new Datum(TEST_YEAR, 04, 16);
        Datum to = new Datum(TEST_YEAR, 04, 17);
        Set<String> carIds = dbRead.findAvailableCar(from, to);
        assertTrue(carIds.size() > 0);
        String carId = carIds.iterator().next();
        assertTrue(dbRead.findAvailableCar(from, to).contains(carId));
        dbWrite.upsertBookingForCar(carId, CUSTOMER_ID_00001002, from, to,
                DatabaseReadInterface.STATE_OPEN);
        assertFalse(dbRead.findAvailableCar(from, to).contains(carId));
    }

    @Test
    public void testReturnCar() {
        Datum from = new Datum(TEST_YEAR, 04, 16);
        Datum to = new Datum(TEST_YEAR, 04, 26);
        assertFalse(dbRead.findAvailableCar(from, to).contains(M_LI_200));
        dbWrite.upsertBookingForCar(M_LI_200, CUSTOMER_ID_00001001, from, to,
                DatabaseReadInterface.STATE_IN_RETURN);

        // Prüfen ob die Rückgabe erfolgreich war
        Set<BookingDTO> bookings = dbRead.getBookingsForCarAsDTOs(M_LI_200);
        assertEquals(1, bookings.size());
        BookingDTO booking = bookings.iterator().next();
        assertEquals(from, booking.getFrom());
        assertEquals(to, booking.getTo());
        // Hier muss jetzt das Ergebnis true sein
        assertEquals(DatabaseReadInterface.STATE_IN_RETURN, booking.getState());
    }

    @Test
    public void testCloseBooking() {
        Datum from = new Datum(TEST_YEAR, 04, 16);
        Datum to = new Datum(TEST_YEAR, 04, 26);
        assertFalse(dbRead.findAvailableCar(from, to).contains(M_LI_200));
        dbWrite.upsertBookingForCar(M_LI_200, CUSTOMER_ID_00001001, from, to,
                DatabaseReadInterface.STATE_CLOSED);

        // Prüfen ob die Rückgabe erfolgreich war
        Set<BookingDTO> bookings = dbRead.getBookingsForCarAsDTOs(M_LI_200);
        assertEquals(1, bookings.size());
        BookingDTO booking = bookings.iterator().next();
        assertEquals(from, booking.getFrom());
        assertEquals(to, booking.getTo());
        // Hier muss jetzt das Ergebnis false sein
        assertEquals(DatabaseReadInterface.STATE_CLOSED, booking.getState());
    }
}
