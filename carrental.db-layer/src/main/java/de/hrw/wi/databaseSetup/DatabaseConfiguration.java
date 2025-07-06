/**
 * 
 */
package de.hrw.wi.databaseSetup;

/**
 * Stores database configuration like database connection string, user, password at a central point.
 * Thus, it can be changed centrally while being accessible from all units needing it: We do not
 * need to spread it over the whole system.
 * 
 * @author Andriessens
 *
 */
public final class DatabaseConfiguration {

    private static final String DB_DRIVER = "jdbc:hsqldb:";
    private static final String DB_URL_IN_PROCESS_MODE_NORMAL_PREFIX = "file:";
    private static final String DB_URL_IN_PROCESS_MODE_NORMAL_INITIAL_PATH =
            "../carrental.db-layer/database/carrental_db";
    private static final String DB_USER_INITIAL = "sa";
    private static final String DB_PASSWORD_INITIAL = "";

    private static String dbUser = DB_USER_INITIAL;
    private static String dbPassword = DB_PASSWORD_INITIAL;
    private static String dbPath = DB_URL_IN_PROCESS_MODE_NORMAL_INITIAL_PATH;
    private static String dbUrl = DB_DRIVER + DB_URL_IN_PROCESS_MODE_NORMAL_PREFIX + dbPath;

    private DatabaseConfiguration() {
        // prevent calling of constructor, therefore visibility private
    }

    /**
     * 
     * @return the user name to use in this database configuration
     */
    public static String getDBUser() {
        return dbUser;
    }

    /**
     * 
     * @return the password for the user of this database configuration
     */
    public static String getDBPassword() {
        return dbPassword;
    }

    /**
     * @return the dbUrl
     */
    public static String getDbUrl() {
        return dbUrl;
    }

}
