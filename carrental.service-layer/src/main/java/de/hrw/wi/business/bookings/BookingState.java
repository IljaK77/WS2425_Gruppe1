package de.hrw.wi.business.bookings;

/**
 * 
 * @author andriesc
 *
 */
public interface BookingState {
	/**
	 * Auto zurückgeben -- das ist nur möglich, wenn die Buchung offen ist.
	 */
	void returnCar();

	/**
	 * Buchung schließen -- das ist nur möglich, wenn das Auto schon
	 * zurückgegeben wurde und die Buchung also nicht mehr offen, aber auch noch
	 * nicht abgeschlossen ist.
	 */
	void closeBooking();

	/**
	 * 
	 * @return <code>true</code>, wenn die Buchung offen ist, <code>false</code>
	 *         sonst
	 */
	boolean isOpen();

	/**
	 * 
	 * @return <code>true</code>, wenn das Auto zurückgegeben wurde,
	 *         <code>false</code> sonst
	 */
	boolean isCarInReturn();

	/**
	 * 
	 * @return <code>true</code>, wenn die Buchung geschlossen ist,
	 *         <code>false</code> sonst
	 */
	boolean isClosed();
}
