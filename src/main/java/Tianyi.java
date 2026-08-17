import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Runs the Tianyi chatbot application.
 */
public class Tianyi {
    public static void print(String content) {
        System.out.println("____________________________________________________________\n"
                + content + "\n"
                + "____________________________________________________________");
    }

    public static void printList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        StringBuilder content = new StringBuilder();

        for (int i = 0; i < list.size() - 1; i += 1) {
            content.append(i + 1)
                    .append(". ")
                    .append(list.get(i))
                    .append("\n");
        }

        content.append(list.size())
                .append(". ")
                .append(list.getLast());

        print(content.toString());
    }

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

        print(banner + "\n" + greeting);

        Scanner scanner = new Scanner(System.in);
        List<String> list = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                break;
            }

            switch (command) {
                case "":
                    break;
                case "list":
                    printList(list);
                    break;
                default:
                    list.add(command);
                    print("added: " + command);
                    break;
            }
        }

        print(farewell);
    }
}
