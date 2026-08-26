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

    public Ui() {
        sc = new Scanner(System.in);
    }

    public boolean hasNextCommand() {
        return sc.hasNextLine();
    }

    public String readCommand() {
        return sc.nextLine();
    }

    public void showWelcome() {
        print(BANNER + "\n" + GREETING);
    }

    public void showResponse(String response) {
        print(response);
    }

    public void showError(String message) {
        print("Oops! " + message);
    }

    public void showGoodbye() {
        print(FAREWELL);
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    private void print(String content) {
        showLine();
        System.out.println(content);
        showLine();
    }
}
