package de.hrw.wi.business.bookings;

/**
 * @author andriesc
 */
public class BookingClosed implements BookingState {
    private final String bookingAlreadyClosed = "Booking has already been closed.";
    @SuppressWarnings("unused")
    private Booking booking;

    /**
     * Unfinished State class
     *
     * @param booking the booking this object is a state for
     */
    public BookingClosed(Booking booking) {
        this.booking = booking;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void returnCar() {
        // TODO Auto-generated method stub
        return;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeBooking() {
        // TODO Auto-generated method stub
        return;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCarInReturn() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isClosed() {
        return true;
    }

}
