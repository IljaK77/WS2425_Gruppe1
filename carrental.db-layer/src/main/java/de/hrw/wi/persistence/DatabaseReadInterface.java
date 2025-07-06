package de.hrw.wi.persistence;

import java.util.Set;

import de.hrw.wi.persistence.dto.BookingDTO;
import de.hrw.wi.types.Datum;

/**
 * 
 * @author andriesc
 *
 */
public interface DatabaseReadInterface {
    // Konstanten, die die Werte für Zustände von Autos enthalten, so dass man
    // nicht mit den Zahlen arbeiten muss
    int STATE_OPEN = 2;
    int STATE_IN_RETURN = 1;
    int STATE_CLOSED = 0;

    /**
     * 
     * @return Eine Liste aller KFZ-Kennzeichen in der Datenbank
     */
    Set<String> getAllCars();

    /**
     * 
     * @param carId
     *            KFZ-Kennzeichen eines Autos
     * @return Die Marke des Autos oder <code>null</code>, wenn das KFZ-Kennzeichen nicht in der
     *         Datenbank vorhanden ist
     */
    String getCarBrand(String carId);

    /**
     * 
     * @param carId
     *            KFZ-Kennzeichen eines Autos
     * @return Alle Buchungen für das Auto, egal mit welchem Zustand (eine leere Menge, wenn es
     *         keine Buchungen gibt)
     */
    Set<BookingDTO> getBookingsForCarAsDTOs(String carId);

    /**
     * 
     * @return Alle Buchungen, egal mit welchen Zustand (eine leere Menge, wenn es keine Buchungen
     *         gibt)
     */
    Set<BookingDTO> getAllBookingsAsDTOs();

    /**
     * 
     * @param carId
     *            KFZ-Kennzeichen eines Autos
     * @param from
     *            Startdatum
     * @param to
     *            Enddatum
     * @return Ist das Auto verfügbar im angegebenen Zeitraum (Anfang / Ende einschließlich)?
     */
    boolean isCarAvailable(String carId, Datum from, Datum to);

    /**
     * 
     * @param from
     *            Startdatum
     * @param to
     *            Enddatum
     * @return Liste der KFZ-Kennzeichen aller Autos, die im angegebenen Zeitraum verfügbar sind
     */
    Set<String> findAvailableCar(Datum from, Datum to);

    /**
     * 
     * @return Die Nummern (IDs) aller Kunden
     */
    Set<String> getAllCustomers();

    /**
     * 
     * @param customerId
     *            ID eines Kunden
     * @return Vorname des Kunden mit der ID
     */
    String getFirstName(String customerId);

    /**
     * 
     * @param customerId
     *            ID eines Kunden
     * @return Nachname des Kunden mit der ID
     */
    String getLastName(String customerId);
}
