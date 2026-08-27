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

    private final Scanner sc;

    /**
     * Creates a console interface that reads from standard input.
     */
    public Ui() {
        sc = new Scanner(System.in);
    }

    /**
     * Reports whether another command is available from standard input.
     *
     * @return {@code true} if another line can be read
     */
    public boolean hasNextCommand() {
        return sc.hasNextLine();
    }

    /**
     * Reads the next complete command line.
     *
     * @return command entered by the user
     */
    public String readCommand() {
        return sc.nextLine();
    }

    /**
     * Displays the application banner and greeting.
     */
    public void showWelcome() {
        print(BANNER + "\n" + GREETING);
    }

    /**
     * Displays a successful command response between separator lines.
     *
     * @param response response to display
     */
    public void showResponse(String response) {
        print(response);
    }

    /**
     * Displays an application error between separator lines.
     *
     * @param message error explanation to display
     */
    public void showError(String message) {
        print("Oops! " + message);
    }

    /**
     * Displays the farewell message.
     */
    public void showGoodbye() {
        print(FAREWELL);
    }

    /**
     * Prints a horizontal separator line.
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays content enclosed by separator lines.
     */
    private void print(String content) {
        showLine();
        System.out.println(content);
        showLine();
    }
}
