package DataAccess;

/**
 * A class that extends the Exception class for instances of failed access into the database
 */

public class DataAccessException extends Exception {
    /**
     * Exception for bad access into the database.
     * @param message
     */
    DataAccessException(String message) {
        super(message);
    }
}
