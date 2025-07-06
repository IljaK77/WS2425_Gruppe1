/**
 * 
 */
package de.hrw.wi.service;

import java.util.Set;

import de.hrw.wi.business.Car;
import de.hrw.wi.business.Customer;
import de.hrw.wi.business.bookings.Booking;
import de.hrw.wi.types.Datum;

/**
 * @author andriesc
 *
 */
public interface CarRentalServiceInterface {

    /**
     * Fügt ein Auto als buchbaren Leihwagen hinzu.
     * 
     * @author andriesc
     * @param car
     *            Das hinzuzufügende Auto
     * @return true, wenn das Auto hinzugefügt werden konnte
     */
    boolean addCar(Car car);

    /**
     * 
     * @return Alle Autos im Verleih, die nicht deaktiviert sind
     */
    Set<Car> getAllCars();

    /**
     * 
     * @param car
     *            das betreffende Auto
     * @param from
     *            Beginn des Zeitraums
     * @param to
     *            Ende des Zeitraums
     * @return <code>true</code>, wenn das Auto im Zeitraum noch nicht ausgeliehen ist
     */
    boolean isCarAvailable(Car car, Datum from, Datum to);

    /**
     * 
     * @param from
     *            Beginn des Zeitraums
     * @param to
     *            Ende des Zeitraums
     * @return die Menge der Autos, die im angegebenen Zeitraum noch nicht ausgeliehen sind
     */
    Set<Car> findAvailableCar(Datum from, Datum to);

    /**
     * 
     * @return Alle derzeit gespeicherten Kunden
     */
    Set<Customer> getAllCustomers();

    /**
     * Zum Verleihen eines Autos. Das Auto muss verfügbar sein, darf also noch nicht im selben
     * Zeitraum ausgeliehen sein! Die erzeugte Buchung bleibt offen, bis das Auto zurückgenommen
     * wurde.
     * 
     * Ein Auto kann an dem Tag, an dem es zurückgebracht wurde, nicht mehr ausgeliehen werden.
     * Deshalb ist es egal, zu welcher Uhrzeit es zurückgebracht oder ausgeliehen wird.
     * 
     * @param car
     *            Das zu buchende Auto
     * @param cust
     *            Der buchende Kunde
     * @param from
     *            Datum, ab dem das Auto ausgeliehen wird (Uhrzeit ist egal)
     * @param to
     *            Datum, zu dem das Auto zurückgebracht wird (Uhrzeit ist egal)
     * @return Die erzeugte Buchung oder <code>null</code>, wenn die Buchung fehlschlug
     */
    Booking bookCar(Car car, Customer cust, Datum from, Datum to);

    /**
     * Rücknahme eines Autos. Auto kann nur zurückgenommen werden, wenn es auch ausgeliehen war und
     * diese Buchung noch nicht abgeschlossen ist. Die passende Buchung wird über das Rückgabedatum
     * gesucht, was im Buchungszeitraum liegen muss.
     * 
     * @param car
     *            Das Auto, das zurückgegeben wird
     * @param returnDate
     *            Datum, an dem das Auto zurückgegeben wird. Das Rückgabedatum muss größer oder
     *            gleich dem Startdatum der Reservierung sein.
     * @return Die Buchung, für die das Auto zurückgenommen wurde oder <code>null</code>, wenn keine
     *         passende Buchung gefunden wurde
     */
    Booking returnCar(Car car, Datum returnDate);

    /**
     * Abschluss der Buchung eines Autos. Das kann nur erfolgen, wenn das Auto vorher schon
     * zurückgenommen wurde und die Buchung noch nicht abgeschlossen ist. Die passende Buchung wird
     * über das Rückgabedatum gesucht, was im Buchungszeitraum liegen muss.
     * 
     * @param car
     *            Das Auto, dessen Buchung geschlossen werden soll
     * @param returnDate
     *            Das Rückgabedatum
     * @return Die Buchung, die abgeschlossen wurde oder <code>null</code>, wenn keine passende
     *         gefunden wurde
     */
    Booking closeBookingForCar(Car car, Datum returnDate);

    /**
     * 
     * @param car
     *            Das Auto, zu dem die Buchungen zurückgegeben werden sollen
     * @return alle Buchungen für das Auto, die noch nicht geschlossen sind (also "offen" oder "Auto
     *         in Rücknahme")
     */
    Set<Booking> getOpenBookingsForCar(Car car);

    /**
     * Gibt alle Buchungen in allen Zuständen für alle Autos zurück
     * 
     * @return Alle Buchungen
     */
    Set<Booking> getAllBookings();

    /**
     * Gibt ein <code>Car</code>-Objekt zu einer ID zurück.
     * 
     * @param carId die ID eines <code>Car</code>-Objektes
     * @return das zugehörige <code>Car</code>-Objekt oder <code>null</code>, wenn kein Objekt mit
     *         dieser ID existiert.
     */
    Car getCarById(String carId);

    /**
     * Gibt ein <code>Customer</code>-Objekt zu einer ID zurück.
     * 
     * @param customerId die ID eines <code>Customer</code>-Objektes
     * @return das zugehörige <code>Customer</code>-Objekt oder <code>null</code>, wenn kein Objekt
     *         mit dieser ID existiert.
     */
    Customer getCustomerById(String customerId);
}
