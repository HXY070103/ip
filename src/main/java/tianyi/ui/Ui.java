package tianyi.ui;

import java.util.Scanner;

/**
 * Handles all console interactions with the user.
 */
public class Ui {
    private static final String BANNER = " _____ _                   _\n"
            + "|_   _(_) __ _ _ __  _   _(_)\n"
            + "  | | | |/ _` | '_ \\| | | | |\n"
            + "  | | | | (_| | | | | |_| | |\n"
            + "  |_| |_|\\__,_|_| |_|\\__, |_|\n"
            + "                     |___/";
    private static final String GREETING = "Hello! I'm Tianyi.\n"
            + "What can I do for you?";
    private static final String FAREWELL = "Bye. Hope to see you again soon!";

    private final Scanner scanner;

    /**
     * Creates a console interface that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reports whether another command is available from standard input.
     *
     * @return {@code true} if another line can be read.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next complete command line.
     *
     * @return Command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the application banner and greeting.
     */
    public void showWelcome() {
        showMessage(BANNER, GREETING);
    }

    /**
     * Displays a successful command response between separator lines.
     *
     * @param response Response to display.
     */
    public void showResponse(String response) {
        showMessage(response);
    }

    /**
     * Displays an application error between separator lines.
     *
     * @param message Error explanation to display.
     */
    public void showError(String message) {
        showMessage("Oops! " + message);
    }

    /**
     * Displays the farewell message.
     */
    public void showGoodbye() {
        showMessage(FAREWELL);
    }

    /**
     * Prints a horizontal separator line.
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays one or more messages enclosed by separator lines.
     *
     * @param messages Messages to display in order.
     */
    private void showMessage(String... messages) {
        showLine();

        for (String message : messages) {
            System.out.println(message);
        }

        showLine();
    }
}
