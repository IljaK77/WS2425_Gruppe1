/**
 *
 */
package de.hrw.wi.service;

import de.hrw.wi.business.Car;
import de.hrw.wi.business.Customer;
import de.hrw.wi.business.bookings.Booking;
import de.hrw.wi.databaseSetup.DatabaseConfiguration;
import de.hrw.wi.persistence.RealDatabase;
import de.hrw.wi.types.Datum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author andriesc
 */
public class CarRentalServiceImplIntegrationTest {
    private static final int TEST_YEAR = 2024;
    private static final String LASTNAME_HOPPER = "Hopper";
    private static final String FIRSTNAME_GRACE = "Grace";
    private static final String LASTNAME_NOETHER = "Noether";
    private static final String FIRSTNAME_EMMY = "Emmy";
    private static final String FIRSTNAME_ALAN = "Alan";
    private static final String LASTNAME_TURING = "Turing";
    private static final String LASTNAME_LOVELACE = "Lovelace";
    private static final String FIRSTNAME_ADA = "Ada";
    private static final String LP_NOT_EXISTING = "FN-LI 656";
    private static final String LP_M_LI_200 = "M-LI 200";
    private static final String LP_KA_PA_656 = "KA-PA 656";
    private static final String LP_FN_TT_999 = "FN-TT 999";
    private static final String LP_RV_HS_1000 = "RV-HS 1000";
    private static final String LP_RV_CA_2015 = "RV-CA 2015";

    private static final String BRAND_BMW = "BMW";
    private static final String BRAND_HONDA = "Honda";
    private static final String BRAND_AUDI = "Audi";
    private static final String CUSTOMER_ID_NOT_EXISTING = "00000001";
    private static final String CUSTOMER_ID_00001000 = "00001000";
    private static final String CUSTOMER_ID_00001001 = "00001001";
    private static final String CUSTOMER_ID_00001002 = "00001002";
    private static final String CUSTOMER_ID_00001003 = "00001003";

    private static final Customer CUSTOMER_HOPPER =
            new Customer(CUSTOMER_ID_00001000, FIRSTNAME_GRACE, LASTNAME_HOPPER);
    private static final Customer CUSTOMER_NOETHER =
            new Customer(CUSTOMER_ID_00001001, FIRSTNAME_EMMY, LASTNAME_NOETHER);
    private static final Customer CUSTOMER_LOVELACE =
            new Customer(CUSTOMER_ID_00001002, FIRSTNAME_ADA, LASTNAME_LOVELACE);
    private static final Customer CUSTOMER_TURING =
            new Customer(CUSTOMER_ID_00001003, FIRSTNAME_ALAN, LASTNAME_TURING);
    private static final Car CAR_KA_PA_656 = new Car(BRAND_HONDA, LP_KA_PA_656);
    private static final Car CAR_M_LI_200 = new Car(BRAND_BMW, LP_M_LI_200);
    private static final Car CAR_FN_TT_999 = new Car(BRAND_AUDI, LP_FN_TT_999);
    private static final Car CAR_RV_HS_1000 = new Car(BRAND_BMW, LP_RV_HS_1000);

    private static final String DB_URL = DatabaseConfiguration.getDbUrl();
    private static final String USER = DatabaseConfiguration.getDBUser();
    private static final String PASSWORD = DatabaseConfiguration.getDBPassword();

    private CarRentalServiceInterface carRentalService;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        RealDatabase db = new RealDatabase();
        carRentalService = new CarRentalServiceImpl(db, db);
    }

    /**
     * TODO A12c): Im Test testAddCar() wird ein neues Auto im System gespeichert.
     * Vervollständigen Sie den Test so, dass er am Ende des Tests überprüft, dass die Datenbank
     * nur wie geplant verändert wurde und dem bereits vorliegenden XML-Schnappschuss afterinsert
     * .xml entspricht.
     *
     * @throws Exception
     */
    @Test
    public void testAddCar() throws Exception {
        Car car = new Car(BRAND_AUDI, LP_RV_CA_2015);
        boolean status = carRentalService.addCar(car);
        assertTrue(status);
    }


    /**
     * TODO A13a) Im Test testAddIllegalCar() wird versucht, fälschlich ein Auto ohne Kennzeichen
     * abzuspeichern. Die Serviceschicht soll dies durch eine Exception vom Typ
     * IllegalArgumentException abwehren. Der Test ist noch nicht vollständig, vervollständigen
     * Sie ihn folgendermaßen: Der Test soll als korrekt durchlaufen, wenn der service-layer wie
     * vorgesehen den richtigen Exception-Typ tatsächlich wirft, und fehlschlagen, wenn dies
     * nicht der Fall ist.
     */
    @Test
    public void testAddIllegalCar() {
        // Kein Kfz-Kennzeichen angegeben, das muss abgewehrt werden
        Car car = new Car(BRAND_BMW, "");
        boolean status = carRentalService.addCar(car);
    }

    @Test
    public void testGetAllCars() {
        Set<Car> cars = carRentalService.getAllCars();
        assertEquals(5, cars.size());
    }

    @Test
    public void testBookCar() {
        Datum from = new Datum(TEST_YEAR, 11, 16);
        Datum to = new Datum(TEST_YEAR, 11, 17);
        Set<Car> cars = carRentalService.findAvailableCar(from, to);
        assertTrue(cars.size() > 0);
        Car rentalCar = cars.iterator().next();
        Booking booking = carRentalService.bookCar(rentalCar, CUSTOMER_LOVELACE, from, to);
        assertEquals(rentalCar.getBrand(), booking.getCar().getBrand());
        assertEquals(rentalCar.getId(), booking.getCar().getId());
        assertEquals(from, booking.getFrom());
        assertEquals(to, booking.getTo());
    }

    @Test
    public void testIsCarAvailable() {
        Datum from = new Datum(TEST_YEAR, 1, 1);
        Datum to = new Datum(TEST_YEAR, 1, 5);
        boolean avail = carRentalService.isCarAvailable(CAR_KA_PA_656, from, to);
        assertTrue(avail);
        from = new Datum(TEST_YEAR, 4, 4);
        to = new Datum(TEST_YEAR, 4, 5);
        avail = carRentalService.isCarAvailable(CAR_KA_PA_656, from, to);
        assertFalse(avail);
    }

    @Test
    public void testFindAvailableCar() {
        Datum from = new Datum(TEST_YEAR, 11, 4);
        Datum to = new Datum(TEST_YEAR, 11, 5);
        Set<Car> cars = carRentalService.findAvailableCar(from, to);
        assertTrue(carRentalService.isCarAvailable(cars.iterator().next(), from, to));
    }

    @Test
    public void testGetAllCustomers() {
        Set<Customer> customers = carRentalService.getAllCustomers();
        assertEquals(4, customers.size());
        boolean hopperFound = false;
        boolean noetherFound = false;
        boolean lovelaceFound = false;
        boolean turingFound = false;
        for (Customer customer : customers) {
            if (customer.getFirstName().equals(FIRSTNAME_EMMY) && customer.getLastName()
                    .equals(LASTNAME_NOETHER)) {
                noetherFound = true;
            }
            if (customer.getFirstName().equals(FIRSTNAME_GRACE) && customer.getLastName()
                    .equals(LASTNAME_HOPPER)) {
                hopperFound = true;
            }
            if (customer.getFirstName().equals(FIRSTNAME_ADA) && customer.getLastName()
                    .equals(LASTNAME_LOVELACE)) {
                lovelaceFound = true;
            }
            if (customer.getFirstName().equals(FIRSTNAME_ALAN) && customer.getLastName()
                    .equals(LASTNAME_TURING)) {
                turingFound = true;
            }

        }
        assertTrue(noetherFound);
        assertTrue(hopperFound);
        assertTrue(lovelaceFound);
        assertTrue(turingFound);
    }

    @Test
    public void testReturnCar() {
        Car car = new Car(BRAND_HONDA, LP_KA_PA_656);
        Datum returnDate = new Datum(TEST_YEAR, 4, 1);
        Booking booking = carRentalService.returnCar(car, returnDate);
        assertNull(booking);
        returnDate = new Datum(TEST_YEAR, 4, 26);
        car = new Car(BRAND_BMW, LP_M_LI_200);
        booking = carRentalService.returnCar(car, returnDate);
        assertTrue(booking.isCarInReturn());
    }

    @Test
    public void testCloseBookingForCar() {
        Car car = CAR_KA_PA_656;
        Datum returnDate = new Datum(TEST_YEAR, 4, 1);
        Booking booking = carRentalService.closeBookingForCar(car, returnDate);
        assertNull(booking);
        returnDate = new Datum(TEST_YEAR, 4, 26);
        car = CAR_M_LI_200;
        carRentalService.returnCar(car, returnDate);
        booking = carRentalService.closeBookingForCar(car, returnDate);
        assertTrue(booking.isClosed());
    }

    @Test
    public void testGetAllBookings() {
        HashSet<Booking> expectedBookings = new HashSet<>();
        Booking booking = new Booking(CAR_KA_PA_656, CUSTOMER_LOVELACE, new Datum(TEST_YEAR, 4, 3),
                new Datum(TEST_YEAR, 4, 6), Booking.InitialBookingState.CLOSED);
        expectedBookings.add(booking);
        booking = new Booking(CAR_M_LI_200, CUSTOMER_NOETHER, new Datum(TEST_YEAR, 4, 16),
                new Datum(TEST_YEAR, 4, 26), Booking.InitialBookingState.OPEN);
        expectedBookings.add(booking);
        booking = new Booking(CAR_FN_TT_999, CUSTOMER_HOPPER, new Datum(TEST_YEAR, 4, 18),
                new Datum(TEST_YEAR, 5, 2), Booking.InitialBookingState.OPEN);
        expectedBookings.add(booking);
        booking = new Booking(CAR_RV_HS_1000, CUSTOMER_TURING, new Datum(TEST_YEAR, 3, 22),
                new Datum(TEST_YEAR, 4, 28), Booking.InitialBookingState.IN_RETURN);
        expectedBookings.add(booking);

        Set<Booking> actualBookings = carRentalService.getAllBookings();
        assertEquals(4, actualBookings.size());
        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    public void testGetCarById() {
        Car actualCar = carRentalService.getCarById(CAR_FN_TT_999.getId());
        assertNotNull(actualCar);
        assertEquals(actualCar, CAR_FN_TT_999);
        actualCar = carRentalService.getCarById(LP_NOT_EXISTING);
        assertNull(actualCar);
    }

    @Test
    public void testGetCustomerById() {
        Customer actualCustomer = carRentalService.getCustomerById(CUSTOMER_TURING.getId());
        assertNotNull(actualCustomer);
        assertEquals(actualCustomer, CUSTOMER_TURING);
        actualCustomer = carRentalService.getCustomerById(CUSTOMER_ID_NOT_EXISTING);
        assertNull(actualCustomer);
    }

}
