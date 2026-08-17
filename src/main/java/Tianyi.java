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

    public static void printTasks(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return;
        }

        StringBuilder content = new StringBuilder();

        for (int i = 0; i < tasks.size(); i += 1) {
            content.append(i + 1)
                    .append(".")
                    .append(tasks.get(i).toString());

            if (i < tasks.size() - 1) {
                content.append("\n");
            }
        }

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

        List<Task> tasks = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            String[] parts = command.split("\\s+", 2);
            String commandWord = parts[0];

            if (commandWord.equals("bye")) {
                break;
            }

            int taskIdx = 0;

            switch (commandWord) {
                case "":
                    break;
                case "list":
                    printTasks(tasks);
                    break;
                case "mark":
                    taskIdx = Integer.parseInt(parts[1]) - 1;
                    tasks.get(taskIdx).markAsDone();
                    print("Nice! I've marked this task as done:\n"
                            + "  " + tasks.get(taskIdx));
                    break;
                case "unmark":
                    taskIdx = Integer.parseInt(parts[1]) - 1;
                    tasks.get(taskIdx).unmarkAsDone();
                    print("OK, I've marked this task as not done yet:\n"
                            + "  " + tasks.get(taskIdx));
                    break;
                default:
                    tasks.add(new Task(command));
                    print("added: " + command);
                    break;
            }
        }

        print(farewell);
    }
}
