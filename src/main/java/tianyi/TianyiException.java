package tianyi;

/**
 * Represents an application-level error in Tianyi.
 */
public class TianyiException extends Exception {
    /**
     * Creates an exception with a message suitable for display to the user.
     *
     * @param message explanation of the application error
     */
    public TianyiException(String message) {
        super(message);
    }
}
