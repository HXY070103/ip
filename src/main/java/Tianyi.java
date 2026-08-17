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
                + "                     |___/\n";
        String greeting = "Hello! I'm Tianyi.\n"
                + "What can I do for you?\n";
        String farewell = "Bye. Hope to see you again soon!\n";
        String divider = "____________________________________________________________\n";

        System.out.println(divider
                + banner
                + greeting
                + divider
                + farewell
                + divider);
    }
}
