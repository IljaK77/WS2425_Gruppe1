package de.hrw.wi.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.hrw.wi.business.Car;
import de.hrw.wi.business.Customer;
import de.hrw.wi.business.bookings.Booking;
import de.hrw.wi.persistence.DatabaseReadInterface;
import de.hrw.wi.persistence.DatabaseWriteInterface;
import de.hrw.wi.persistence.PersistenceException;
import de.hrw.wi.persistence.dto.BookingDTO;
import de.hrw.wi.types.Datum;

/**
 * 
 * @author andriesc
 *
 */
public class CarRentalServiceImpl implements CarRentalServiceInterface {

    DatabaseReadInterface dbRead;
    DatabaseWriteInterface dbWrite;

    /**
     * 
     * @param dbRead
     *            the reading interface for the database
     * @param dbWrite
     *            the writing interface for the database
     */
    public CarRentalServiceImpl(DatabaseReadInterface dbRead, DatabaseWriteInterface dbWrite) {
        this.dbRead = dbRead;
        this.dbWrite = dbWrite;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addCar(Car car) {
        if (car.getId().equals("") || car.getBrand().equals("")) {
            throw new IllegalArgumentException("Car is not valid.");
        }
        try {
            dbWrite.addCar(car.getId(), car.getBrand().toString());
            return true;
        } catch (PersistenceException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Car> getAllCars() {
        Set<Car> cars = new HashSet<Car>();
        for (String id : dbRead.getAllCars()) {
            cars.add(new Car(dbRead.getCarBrand(id), id));
        }
        return cars;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCarAvailable(Car car, Datum from, Datum to) {
        return dbRead.isCarAvailable(car.getId(), from, to);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Car> findAvailableCar(Datum from, Datum to) {
        Set<Car> cars = new HashSet<Car>();
        for (String id : dbRead.findAvailableCar(from, to)) {
            cars.add(new Car(dbRead.getCarBrand(id), id));
        }
        return cars;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Customer> getAllCustomers() {
        Set<Customer> customers = new HashSet<Customer>();
        for (String id : dbRead.getAllCustomers()) {
            customers.add(new Customer(id, dbRead.getFirstName(id), dbRead.getLastName(id)));
        }
        return customers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Booking bookCar(Car car, Customer cust, Datum from, Datum to) {
        if (isCarAvailable(car, from, to)) {
            // Autobuchung anlegen
            dbWrite.upsertBookingForCar(car.getId(), cust.getId(), from, to,
                    DatabaseReadInterface.STATE_OPEN);
            return new Booking(car, cust, from, to);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Booking returnCar(Car car, Datum returnDate) {
        Set<Booking> bookings = getOpenBookingsForCar(car);
        Iterator<Booking> iter = bookings.iterator();
        Booking bookingResult = null;
        while (iter.hasNext() && (bookingResult == null)) {
            Booking booking = iter.next();
            if (booking.isOpen() && booking.getFrom().isBefore(returnDate)) {
                // Zugehörige Buchung gefunden, Auto zurückgeben
                dbWrite.upsertBookingForCar(car.getId(), booking.getCustomer().getId(),
                        booking.getFrom(), booking.getTo(), DatabaseReadInterface.STATE_IN_RETURN);
                booking.returnCar();
                bookingResult = booking;
            }
        }
        return bookingResult;
    }

    private Booking.InitialBookingState getInitialBookingStateForDBBookingState(
            int dbBookingState) {
        switch (dbBookingState) {
            case DatabaseReadInterface.STATE_OPEN:
                return Booking.InitialBookingState.OPEN;
            case DatabaseReadInterface.STATE_IN_RETURN:
                return Booking.InitialBookingState.IN_RETURN;
            case DatabaseReadInterface.STATE_CLOSED:
                return Booking.InitialBookingState.CLOSED;
            default:
                throw new IllegalStateException("Illegal state for booking.");
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Booking> getOpenBookingsForCar(Car car) {
        Set<Booking> result = new HashSet<Booking>();
        Set<BookingDTO> bookingDtos = dbRead.getBookingsForCarAsDTOs(car.getId());
        for (BookingDTO dto : bookingDtos) {
            if ((dto.getState() == DatabaseReadInterface.STATE_OPEN)
                    || (dto.getState() == DatabaseReadInterface.STATE_IN_RETURN)) {
                Customer customer =
                        new Customer(dto.getCustomerId(), dbRead.getFirstName(dto.getCustomerId()),
                                dbRead.getLastName(dto.getCustomerId()));
                Booking booking = new Booking(car, customer, dto.getFrom(), dto.getTo(),
                        getInitialBookingStateForDBBookingState(dto.getState()));
                result.add(booking);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Booking closeBookingForCar(Car car, Datum returnDate) {
        Set<Booking> bookings = getOpenBookingsForCar(car);
        Iterator<Booking> iter = bookings.iterator();
        Booking bookingResult = null;
        while (iter.hasNext() && (bookingResult == null)) {
            Booking booking = iter.next();
            if (booking.isCarInReturn() && booking.getFrom().isBefore(returnDate)) {
                // Zugehörige Buchung gefunden, Auto zurückgeben
                dbWrite.upsertBookingForCar(car.getId(), booking.getCustomer().getId(),
                        booking.getFrom(), booking.getTo(), DatabaseReadInterface.STATE_CLOSED);
                booking.closeBooking();
                bookingResult = booking;
            }
        }
        return bookingResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Booking> getAllBookings() {
        Set<Booking> result = new HashSet<Booking>();
        Set<BookingDTO> bookingDtos = dbRead.getAllBookingsAsDTOs();
        for (BookingDTO dto : bookingDtos) {
            Car car = new Car(dbRead.getCarBrand(dto.getCarId()), dto.getCarId());
            Customer customer =
                    new Customer(dto.getCustomerId(), dbRead.getFirstName(dto.getCustomerId()),
                            dbRead.getLastName(dto.getCustomerId()));
            Booking booking = new Booking(car, customer, dto.getFrom(), dto.getTo(),
                    getInitialBookingStateForDBBookingState(dto.getState()));
            result.add(booking);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Car getCarById(String carId) {
        String brand = dbRead.getCarBrand(carId);
        if (!brand.equals("")) {
            Car car = new Car(brand, carId);
            return car;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer getCustomerById(String customerId) {
        String lastName = dbRead.getLastName(customerId);
        String firstName = dbRead.getFirstName(customerId);
        if ((!lastName.equals("")) && (!firstName.equals(""))) {
            Customer customer = new Customer(customerId, firstName, lastName);
            return customer;
        } else {
            return null;
        }
    }

}
