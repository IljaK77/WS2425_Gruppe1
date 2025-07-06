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
public class CarInReturn implements BookingState {
    private Booking booking;

    /**
     * @param booking the booking object this state object belongs to
     */
    public CarInReturn(Booking booking) {
        this.booking = booking;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void returnCar() {
        throw new IllegalStateException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeBooking() {
        this.booking.setState(new BookingClosed(booking));
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
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isClosed() {
        return false;
    }

}
