package de.hrw.wi.persistence;

/**
 * 
 * @author Andriessens
 *
 */
public class PersistenceException extends RuntimeException {

    private static final long serialVersionUID = 4409868268974540924L;

    /**
     * Constructor: Only call super constructor
     */
    public PersistenceException() {
        super();
    }

    /**
     * 
     * @param message
     *            the exception message
     * @param cause
     *            the cause. (A <code>null</code> value is permitted,and indicates that the cause is
     *            nonexistent or unknown.)
     * @param enableSuppression
     *            whether or not suppression is enabledor disabled
     * @param writableStackTrace
     *            whether or not the stack trace shouldbe writable
     */
    public PersistenceException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

    /**
     * 
     * @param message
     *            the exception message
     * @param cause
     *            the cause. (A <code>null</code> value is permitted,and indicates that the cause is
     *            nonexistent or unknown.)
     */
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);

    }

    /**
     * 
     * @param message
     *            the exception message
     */
    public PersistenceException(String message) {
        super(message);

    }

    /**
     * 
     * @param cause
     *            the cause. (A <code>null</code> value is permitted,and indicates that the cause is
     *            nonexistent or unknown.)
     */
    public PersistenceException(Throwable cause) {
        super(cause);

    }

}
