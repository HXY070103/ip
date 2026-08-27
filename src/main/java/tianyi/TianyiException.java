package tianyi;

/**
 * Represents an application-level error in Tianyi.
 */
public class TianyiException extends Exception {
    /**
     * Creates an exception with the specified user-facing message.
     */
    public TianyiException(String message) {
        super(message);
    }
}
