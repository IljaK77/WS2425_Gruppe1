/**
 * 
 */
package de.hrw.wi.persistence.dto;

import de.hrw.wi.types.Datum;

/**
 * DataTransferObject (DTO) für Buchungen
 * 
 * @author andriesc
 *
 */
public class BookingDTO {
    private int state;
    private String carId;
    private String customerId;
    private Datum from;
    private Datum to;

    /**
     * 
     * @param state
     *                        Der Zustand der Buchung wie definiert über die Konstanten
     *                        <code>DatabaseReadInterface.STATE_OPEN</code>,
     *                        <code>DatabaseReadInterface.STATE_IN_RETURN</code>
     *                        <code>DatabaseReadInterface.STATE_CLOSED</code>
     * 
     * @param carId
     *                        Die eindeutige ID des Autos
     * @param from
     *                        Anfangsdatum der Buchung
     * @param to
     *                        Enddatum der Buchung
     * @param customerId
     *                        Die eindeutige ID des Kunden
     */
    public BookingDTO(int state, String carId, Datum from, Datum to, String customerId) {
        this.state = state;
        this.carId = carId;
        this.customerId = customerId;
        this.from = from;
        this.to = to;
    }

    /**
     * 
     * @return Der Zustand der Buchung wie definiert über die Konstanten
     *         <code>DatabaseReadInterface.STATE_OPEN</code>,
     *         <code>DatabaseReadInterface.STATE_IN_RETURN</code>
     *         <code>DatabaseReadInterface.STATE_CLOSED</code>
     */
    public int getState() {
        return state;
    }

    /**
     * 
     * @return Die eindeutige ID des Autos
     */
    public String getCarId() {
        return carId;
    }

    /**
     * 
     * @return Das Anfangsdatum der Buchung
     */
    public Datum getFrom() {
        return from;
    }

    /**
     * 
     * @return Das Enddatum der Buchung
     */
    public Datum getTo() {
        return to;
    }

    /**
     * 
     * @return Die eindeutige Kunden-ID
     */
    public String getCustomerId() {
        return customerId;
    }

}
