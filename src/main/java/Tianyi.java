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

        StringBuilder content = new StringBuilder("Here are the tasks in your list:\n");

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
            String input = scanner.nextLine().trim();
            String[] inputParts = input.split("\\s+", 2);
            String command = inputParts[0];

            if (command.equals("bye")) {
                break;
            }

            int taskIdx = 0;

            switch (command) {
                case "":
                    break;
                case "list":
                    printTasks(tasks);
                    break;
                case "todo", "deadline", "event":
                    Task task = null;

                    switch (command) {
                        case "todo":
                            task = new ToDo(inputParts[1]);
                            break;
                        case "deadline":
                            String[] deadlineParts = inputParts[1].split("\\s+/by\\s+", 2);
                            task = new Deadline(deadlineParts[0], deadlineParts[1]);
                            break;
                        case "event":
                            String[] eventParts = inputParts[1].split("\\s+/from\\s+", 2);
                            String[] timeParts = eventParts[1].split("\\s+/to\\s+", 2);
                            task = new Event(eventParts[0], timeParts[0], timeParts[1]);
                            break;
                    }

                    tasks.add(task);
                    print("Got it. I've added this task:\n"
                            + "  " + task.toString() + "\n"
                            + "Now you have " + tasks.size() + " tasks in the list.");
                    break;
                case "mark", "unmark":
                    taskIdx = Integer.parseInt(inputParts[1]) - 1;

                    switch (command) {
                        case "mark":
                            tasks.get(taskIdx).markAsDone();
                            print("Nice! I've marked this task as done:\n"
                                    + "  " + tasks.get(taskIdx));
                            break;
                        case "unmark":
                            tasks.get(taskIdx).unmarkAsDone();
                            print("OK, I've marked this task as not done yet:\n"
                                    + "  " + tasks.get(taskIdx));
                            break;
                    }
                    break;
                default:
                    tasks.add(new Task(input));
                    print("added: " + input);
                    break;
            }
        }

        print(farewell);
    }
}
