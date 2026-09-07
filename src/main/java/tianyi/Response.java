package tianyi;

/**
 * Represents the result of processing a user command.
 */
public class Response {
    private final String message;
    private final boolean isExit;

    /**
     * Creates a response with its display message and exit status.
     *
     * @param message Message to display to the user.
     * @param isExit Whether the application should exit after displaying the message.
     */
    public Response(String message, boolean isExit) {
        this.message = message;
        this.isExit = isExit;
    }

    /**
     * Returns the message to display to the user.
     *
     * @return Response message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Reports whether the application should exit after this response.
     *
     * @return {@code true} if the application should exit, otherwise {@code false}.
     */
    public boolean isExit() {
        return isExit;
    }
}
