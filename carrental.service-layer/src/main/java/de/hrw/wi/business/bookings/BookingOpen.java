/**
 *
 */
package de.hrw.wi.business.bookings;

/**
 * TODO A11a): Vervollständigen Sie die Implementierung von BookingOpen und CarInReturn in
 * den Methoden returnCar() und closeBooking() und entfernen Sie dabei die Kommentare „// TODO
 * Auto-generated method stub“.
 *
 * @author andriesc
 */
public class BookingOpen implements BookingState {
    private Booking booking;

    /**
     * @param booking the booking object this state object belongs to
     */
    public BookingOpen(Booking booking) {
        this.booking = booking;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void returnCar() {
        this.booking.setState(new CarInReturn(booking));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeBooking() {
        throw new IllegalStateException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen() {
        return true;
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
        return false;
    }

}
