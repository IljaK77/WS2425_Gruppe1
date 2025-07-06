/**
 *
 */
package de.hrw.wi.persistence;

import de.hrw.wi.databaseSetup.DatabaseConfiguration;
import de.hrw.wi.databaseSetup.InitialDatabaseSetup;
import de.hrw.wi.persistence.dto.BookingDTO;
import de.hrw.wi.types.Datum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author andriesc
 */
public class DatabaseReadInterfaceTest {

    private static final int TEST_YEAR = 2024;
    private static final String FN_TT_999 = "FN-TT 999";
    private static final String KA_GB_652 = "KA-GB 652";
    private static final String KA_PA_656 = "KA-PA 656";
    private static final String RV_HS_1000 = "RV-HS 1000";
    private static final String M_LI_200 = "M-LI 200";
    private static final String CUSTOMER_ID_00001003 = "00001003";
    private static final String CUSTOMER_ID_00001002 = "00001002";
    private static final String CUSTOMER_ID_00001001 = "00001001";
    private static final String CUSTOMER_ID_00001000 = "00001000";

    private static final String DB_URL = DatabaseConfiguration.getDbUrl();
    private static final String USER = DatabaseConfiguration.getDBUser();
    private static final String PASSWORD = DatabaseConfiguration.getDBPassword();

    DatabaseReadInterface db;

    /**
     * Initialisiert Datenbank einmal vor Beginn der Tests
     *
     * @throws Exception
     */
    @BeforeAll
    public static void setUpClass() throws Exception {
        // Show current working directory
        String currentPath = System.getProperty("user.dir");
        System.out.println("Current working directory: " + currentPath);
        // Show database URL
        System.out.println("Database URL: " + DB_URL);

        InitialDatabaseSetup.main(null);
    }

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        db = new RealDatabase();
    }

    @Test
    public void testGetAllCars() {
        Set<String> cars = db.getAllCars();
        assertNotNull(cars);
        assertEquals(5, cars.size());
        assertTrue(cars.contains(M_LI_200));
        assertTrue(cars.contains(RV_HS_1000));
        assertTrue(cars.contains(KA_PA_656));
        assertTrue(cars.contains(KA_GB_652));
        assertTrue(cars.contains(FN_TT_999));
    }

    @Test
    public void testGetCarBrand() {
        String brand = db.getCarBrand(KA_GB_652);
        assertEquals("VW", brand);
        brand = db.getCarBrand(FN_TT_999);
        assertEquals("Audi", brand);
    }

    @Test
    public void testGetBookingsForCarAsDTOs() {
        Datum from = new Datum(TEST_YEAR, 4, 18);
        Datum to = new Datum(TEST_YEAR, 5, 2);
        Set<BookingDTO> bookings = db.getBookingsForCarAsDTOs(FN_TT_999);
        assertEquals(1, bookings.size());
        BookingDTO booking = bookings.iterator().next();
        assertEquals(FN_TT_999, booking.getCarId());
        assertEquals(from, booking.getFrom());
        assertEquals(to, booking.getTo());
        assertTrue(booking.getState() == DatabaseReadInterface.STATE_OPEN);
    }

    @Test
    public void testIsCarAvailable() {
        Datum from = new Datum(TEST_YEAR, 1, 1);
        Datum to = new Datum(TEST_YEAR, 1, 5);
        Boolean avail = db.isCarAvailable(KA_PA_656, from, to);
        assertTrue(avail);
        from = new Datum(TEST_YEAR, 04, 4);
        to = new Datum(TEST_YEAR, 4, 5);
        avail = db.isCarAvailable(KA_PA_656, from, to);
        assertFalse(avail);
    }

    @Test
    public void testFindAvailableCar() {
        Datum from = new Datum(TEST_YEAR, 4, 7);
        Datum to = new Datum(TEST_YEAR, 4, 15);
        Set<String> availCars = db.findAvailableCar(from, to);
        assertEquals(4, availCars.size());
        assertTrue(availCars.contains(M_LI_200));
        assertTrue(availCars.contains(KA_GB_652));
        assertTrue(availCars.contains(KA_PA_656));
        assertTrue(availCars.contains(FN_TT_999));
    }

    @Test
    public void testGetAllCustomers() {
        Set<String> customers = db.getAllCustomers();
        assertEquals(4, customers.size());
        assertTrue(customers.contains(CUSTOMER_ID_00001000));
        assertTrue(customers.contains(CUSTOMER_ID_00001001));
        assertTrue(customers.contains(CUSTOMER_ID_00001002));
        assertTrue(customers.contains(CUSTOMER_ID_00001003));
    }

    @Test
    public void testGetFirstName() {
        assertEquals("Grace", db.getFirstName(CUSTOMER_ID_00001000));
        assertEquals("Emmy", db.getFirstName(CUSTOMER_ID_00001001));
        assertEquals("Ada", db.getFirstName(CUSTOMER_ID_00001002));
    }

    @Test
    public void testGetLastName() {
        assertEquals("Hopper", db.getLastName(CUSTOMER_ID_00001000));
        assertEquals("Noether", db.getLastName(CUSTOMER_ID_00001001));
        assertEquals("Lovelace", db.getLastName(CUSTOMER_ID_00001002));
    }

    @Test
    public void testGetAllBookingsAsDTOs() {
        Set<BookingDTO> bookings = db.getAllBookingsAsDTOs();
        assertEquals(4, bookings.size());
        for (BookingDTO booking : bookings) {
            switch (booking.getCarId()) {
                case KA_PA_656:
                    assertEquals(booking.getState(), DatabaseReadInterface.STATE_CLOSED);
                    assertEquals(booking.getCustomerId(), CUSTOMER_ID_00001002);
                    assertEquals(booking.getFrom(), new Datum(TEST_YEAR, 4, 3));
                    assertEquals(booking.getTo(), new Datum(TEST_YEAR, 4, 6));
                    break;
                case M_LI_200:
                    assertEquals(booking.getState(), DatabaseReadInterface.STATE_OPEN);
                    assertEquals(booking.getCustomerId(), CUSTOMER_ID_00001001);
                    assertEquals(booking.getFrom(), new Datum(TEST_YEAR, 4, 16));
                    assertEquals(booking.getTo(), new Datum(TEST_YEAR, 4, 26));
                    break;
                case FN_TT_999:
                    assertEquals(booking.getState(), DatabaseReadInterface.STATE_OPEN);
                    assertEquals(booking.getCustomerId(), CUSTOMER_ID_00001000);
                    assertEquals(booking.getFrom(), new Datum(TEST_YEAR, 4, 18));
                    assertEquals(booking.getTo(), new Datum(TEST_YEAR, 5, 2));
                    break;
                case RV_HS_1000:
                    assertEquals(booking.getState(), DatabaseReadInterface.STATE_IN_RETURN);
                    assertEquals(booking.getCustomerId(), CUSTOMER_ID_00001003);
                    assertEquals(booking.getFrom(), new Datum(TEST_YEAR, 3, 22));
                    assertEquals(booking.getTo(), new Datum(TEST_YEAR, 4, 28));
                    break;
                default:
                    fail();
            }
        }
    }
}
