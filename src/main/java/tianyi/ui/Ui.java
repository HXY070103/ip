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
     * Creates a console UI that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another console command is available.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Returns the next console command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows the application banner and greeting.
     */
    public void showWelcome() {
        print(BANNER + "\n" + GREETING);
    }

    /**
     * Shows a successful response between separator lines.
     */
    public void showResponse(String response) {
        print(response);
    }

    /**
     * Shows an error message between separator lines.
     */
    public void showError(String message) {
        print("Oops! " + message);
    }

    /**
     * Shows the farewell message.
     */
    public void showGoodbye() {
        print(FAREWELL);
    }

    /**
     * Shows a separator line.
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    private void print(String content) {
        showLine();
        System.out.println(content);
        showLine();
    }
}
