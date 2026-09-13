package tianyi;

/**
 * Represents the result of processing a user command.
 */
public class Response {
    private final String message;
    private final boolean isExit;
    private final boolean isError;

    /**
     * Creates a response with its display message and exit status.
     *
     * @param message Message to display to the user.
     * @param isExit Whether the application should exit after displaying the message.
     */
    public Response(String message, boolean isExit) {
        this(message, isExit, false);
    }

    /**
     * Creates a response with explicit exit and error status.
     *
     * @param message Message to display to the user.
     * @param isExit Whether the application should exit after displaying the message.
     * @param isError Whether the response reports a failed command.
     */
    public Response(String message, boolean isExit, boolean isError) {
        this.isError = isError;
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
     * Reports whether this response describes a failed command.
     *
     * @return Whether the GUI should highlight the response as an error.
     */
    public boolean isError() {
        return isError;
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
