package de.hrw.wi.databaseSetup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.hrw.wi.persistence.DatabaseReadInterface;

/**
 * TODO A3a): Führen Sie im db-layer im Package de.hrw.wi.databaseSetup die Klasse
 * InitialDatabaseSetup aus, um die Datenbank zu initialisieren.
 *
 * @author andriesc
 */
public final class InitialDatabaseSetup {
    private static final String DB_URL = DatabaseConfiguration.getDbUrl();
    private static final String USER = DatabaseConfiguration.getDBUser();
    private static final String PASSWORD = DatabaseConfiguration.getDBPassword();

    private static final String TEST_YEAR = "2024";

    private InitialDatabaseSetup() {
        // vermeiden, dass der Konstruktur dieser Hilfsklasse aufgerufen wird:
        // Deshalb Sichtbarkeit private
    }

    /**
     * Erzeugt Datenbank mit Test-Daten.
     *
     * @param args wird nicht ausgewertet
     * @throws SQLException Bei Problemen mit der DB-Erzeugung
     */
    public static void main(String[] args) throws SQLException {
        Connection c = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        // Show current working directory
        String currentPath = System.getProperty("user.dir");
        System.out.println("Current working directory: " + currentPath);
        // Show database URL
        System.out.println("Database URL: " + DB_URL);
        c.setAutoCommit(false);
        System.out.println("Autocommit " + (c.getAutoCommit() ? "on" : "off"));

        // Alle benötigten Tabellen löschen, falls alte vorhanden sind
        c.createStatement().executeQuery("DROP TABLE BOOKINGS IF EXISTS");
        c.createStatement().executeQuery("DROP TABLE CARS IF EXISTS");
        c.createStatement().executeQuery("DROP TABLE CUSTOMERS IF EXISTS");

        // Tabelle für Autos anlegen
        c.createStatement().executeQuery("CREATE TABLE CARS "
                + "(id VARCHAR(12) PRIMARY KEY, brand VARCHAR(80), active BOOLEAN)");

        // Beispieldaten in Tabelle für Autos einfügen
        c.createStatement().executeQuery("INSERT INTO CARS VALUES ('KA-PA 656', 'Honda', TRUE)");
        c.createStatement().executeQuery("INSERT INTO CARS VALUES ('KA-GB 652', 'VW', TRUE)");
        c.createStatement().executeQuery("INSERT INTO CARS VALUES ('M-LI 200', 'BMW', TRUE)");
        c.createStatement().executeQuery("INSERT INTO CARS VALUES ('RV-HS 1000', 'BMW', TRUE)");
        c.createStatement().executeQuery("INSERT INTO CARS VALUES ('FN-TT 999', 'Audi', TRUE)");

        // Tabelle für Kunden anlegen
        c.createStatement().executeQuery("CREATE TABLE CUSTOMERS "
                + "(id VARCHAR(8) PRIMARY KEY, name VARCHAR(80), firstName VARCHAR(80))");

        // Beispieldaten für Kunden einfügen
        c.createStatement()
                .executeQuery("INSERT INTO CUSTOMERS VALUES ('00001000', 'Hopper', 'Grace')");
        c.createStatement()
                .executeQuery("INSERT INTO CUSTOMERS VALUES ('00001001', 'Noether', 'Emmy')");
        c.createStatement()
                .executeQuery("INSERT INTO CUSTOMERS VALUES ('00001002', 'Lovelace', 'Ada')");
        c.createStatement()
                .executeQuery("INSERT INTO CUSTOMERS VALUES ('00001003', 'Turing', 'Alan')");

        // Tabelle für Buchungen anlegen
        c.createStatement().executeQuery(
                "CREATE TABLE BOOKINGS " + "(car_id VARCHAR(12), customer_id VARCHAR(8),"
                        + " start DATE, end DATE, state INTEGER, "
                        + " constraint PK_STOCK PRIMARY KEY (car_id, start, end),"
                        + " constraint FK_CARS FOREIGN KEY (car_id) REFERENCES CARS(id),"
                        + " constraint FK_CUSTOMERS FOREIGN KEY (customer_id) REFERENCES CUSTOMERS"
                        + "(id))");

        // Feld state in Bookings:
        // 0 = Buchung abgeschlossen
        // 1 = Auto in Rücknahme
        // 2 = Buchung offen (d.h. Auto gebucht, aber noch nicht vom Kunden abgeholt oder Auto schon
        // ausgegeben)

        // Beispieldaten für Buchungen einfügen
        c.createStatement().executeQuery(
                "INSERT INTO BOOKINGS VALUES ('KA-PA 656','00001002', '" + TEST_YEAR + "-04-03', '"
                        + TEST_YEAR + "-04-06', " + DatabaseReadInterface.STATE_CLOSED + ")");
        c.createStatement().executeQuery(
                "INSERT INTO BOOKINGS VALUES ('M-LI 200','00001001', '" + TEST_YEAR + "-04-16', '"
                        + TEST_YEAR + "-04-26', " + DatabaseReadInterface.STATE_OPEN + ")");
        c.createStatement().executeQuery(
                "INSERT INTO BOOKINGS VALUES ('FN-TT 999','00001000', '" + TEST_YEAR + "-04-18', '"
                        + TEST_YEAR + "-05-02', " + DatabaseReadInterface.STATE_OPEN + ")");
        c.createStatement().executeQuery(
                "INSERT INTO BOOKINGS VALUES ('RV-HS 1000','00001003', '" + TEST_YEAR + "-03-22', '"
                        + TEST_YEAR + "-04-28', " + DatabaseReadInterface.STATE_IN_RETURN + ")");

        // Abschliessen
        c.commit();
        c.close();
        System.out.println("ready");

    }
}
