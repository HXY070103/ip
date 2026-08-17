import java.util.Scanner;

/**
 * Runs the Tianyi chatbot application.
 */
public class Tianyi {
    public static void main(String[] args) {
        String banner = " _____ _                   _\n"
                + "|_   _(_) __ _ _ __  _   _(_)\n"
                + "  | | | |/ _` | '_ \\| | | | |\n"
                + "  | | | | (_| | | | | |_| | |\n"
                + "  |_| |_|\\__,_|_| |_|\\__, |_|\n"
                + "                     |___/";
        String greeting = "Hello! I'm Tianyi.\n"
                + "What can I do for you?";
        String farewell = "Bye. Hope to see you again soon!";
        String divider = "____________________________________________________________";

        System.out.println(divider + "\n"
                + banner + "\n"
                + greeting + "\n"
                + divider);

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                break;
            }

            System.out.println(divider + "\n"
                    + command + "\n"
                    + divider);
        }

        System.out.println(divider + "\n"
                + farewell + "\n"
                + divider);
    }
}
