package de.hrw.wi.persistence;

import de.hrw.wi.types.Datum;

/**
 * 
 * @author andriesc
 *
 */
public interface DatabaseWriteInterface {
	/**
	 * 
	 * Fügt ein Auto als verleihbares Fahrzeug hinzu.
	 * 
	 * @param carId
	 *            KFZ-Kennzeichen, muss eindeutig sein
	 * @param brand
	 *            die Automarke
	 * @throws PersistenceException
	 * 
	 */
	void addCar(String carId, String brand) throws PersistenceException;

	/**
	 * Falls schon eine Buchung für das Auto mit ID <code>car_id</code> und für
	 * den Zeitraum <code>from</code>-<code>to</code> in der Datenbank vorhanden
	 * ist, wird diese mit den angegebenen Daten aktualisiert ("UPdate"). Falls
	 * die Buchung noch nicht vorhanden ist, wird sie eingefügt ("inSERT").
	 * 
	 * Mit der Methode können also weder der Zeitraum noch die Auto-ID verändert
	 * werden, nur der Kunde oder der Status der Buchung.
	 * 
	 * @param carId
	 *            Kfz-Kennzeichen des Autos
	 * @param customerId
	 *            Kundennummer des Kunden, für den die Buchung gemacht wird
	 * @param from
	 *            Startdatum der Buchung
	 * @param to
	 *            Enddatum der Buchung
	 * @param state
	 *            Der Status der Buchung (vgl.
	 *            <code>DatabaseReadInterface</code>)
	 * @throws PersistenceException
	 */
	void upsertBookingForCar(String carId, String customerId, Datum from, Datum to, int state)
			throws PersistenceException;

}
