/**
 * 
 */
package de.hrw.wi.types;

import java.time.LocalDate;

/**
 * Ein ganz einfacher Typ für Datumswerte. Er verwendet intern den Standard-Java-Datumstyp
 * <code>LocalDate</code>. Datumswerte können nicht verändert werden.
 * 
 * @author andriesc
 * 
 */
public class Datum {
    private LocalDate date;

    /**
     * Initialisiert ein <code>Datum</code> aus einem <code>LocalDate</code>
     * 
     * @param date
     *            ein <code>LocalDate</code>-Datum zur Initialisierung
     */
    public Datum(LocalDate date) {
        this.date = date;
    }

    /**
     * Initialisiert ein Datum
     * 
     * @param jahr
     *            Das Jahr des Datums
     * @param monat
     *            Der Monat des Datums
     * @param tagImMonat
     *            Die Nummer des Tages im Monat
     */
    public Datum(int jahr, int monat, int tagImMonat) {
        this.date = LocalDate.of(jahr, monat, tagImMonat);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Datum) {
            return this.date.isEqual(((Datum) obj).date);
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.date.hashCode();
    }

    /**
     * 
     * @param other
     *            ein anderes Datum, mit dem verglichen werden soll
     * @return true, wenn das Datum vor dem Datum other liegt
     */
    public boolean isBefore(Datum other) {
        return this.hashCode() < other.hashCode();
    }

    /**
     * 
     * @return Das Jahr zum gespeicherten Datum
     */
    public int getJahr() {
        return this.date.getYear();
    }

    /**
     * 
     * @return Die Zahl 1...12 des Monats zum gespeicherten Datum
     */
    public int getMonat() {
        return this.date.getMonthValue();
    }

    /**
     * 
     * @return Einen Wert zwischen 1...31, der den Tag im Monat zum gespeicherten Datum angibt
     */
    public int getTagImMonat() {
        return this.date.getDayOfMonth();
    }

    /**
     * Outputs this date as a String, such as <code>2007-12-03</code>. The output will be in the
     * ISO-8601 format <code>uuuu-MM-dd</code>.
     * 
     * @return a string representation of this date, not null
     */
    @Override
    public String toString() {
        return date.toString();
    }

}
