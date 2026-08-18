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

    private static String getArgument(String[] inputParts, String errorMessage)
            throws TianyiException {
        if (inputParts.length < 2 || inputParts[1].isBlank()) {
            throw new TianyiException(errorMessage);
        }

        return inputParts[1];
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

            if (input.isEmpty()) {
                continue;
            }

            try {
                String[] inputParts = input.split("\\s+", 2);
                String command = inputParts[0];

                if (command.equals("bye")) {
                    if (inputParts.length > 1 && !inputParts[1].isBlank()) {
                        throw new TianyiException("The bye command does not accept any arguments.\n"
                                + "Try: bye");
                    }

                    break;
                }

                switch (command) {
                    // List current tasks
                    case "list":
                        if (inputParts.length > 1 && !inputParts[1].isBlank()) {
                            throw new TianyiException("The list command does not accept any arguments.\n"
                                    + "Try: list");
                        }

                        printTasks(tasks);
                        break;

                    // Add a task
                    case "todo", "deadline", "event":
                        String taskFormat = null;

                        switch (command) {
                            case "todo":
                                taskFormat = "todo borrow book";
                                break;
                            case "deadline":
                                taskFormat = "deadline return book /by Sunday";
                                break;
                            case "event":
                                taskFormat = "event project meeting /from Mon 2pm /to 4pm";
                                break;
                        }

                        if (inputParts.length < 2 || inputParts[1].isBlank()) {
                            throw new TianyiException("The description of a " + command + " cannot be empty.\n"
                                    + "Try: " + taskFormat);
                        }

                        Task task = null;

                        switch (command) {
                            case "todo":
                                task = new ToDo(inputParts[1]);
                                break;
                            case "deadline":
                                String[] deadlineParts = inputParts[1].split("\\s+/by\\s+", 2);

                                if (deadlineParts.length < 2 || deadlineParts[1].isBlank()) {
                                    throw new TianyiException("A deadline must contain /by time.\n"
                                            + "Try: " + taskFormat);
                                }

                                task = new Deadline(deadlineParts[0], deadlineParts[1]);
                                break;
                            case "event":
                                boolean hasFrom = inputParts[1].contains("/from");
                                boolean hasTo = inputParts[1].contains("/to");

                                if (!hasFrom && !hasTo) {
                                    throw new TianyiException("An event must contain /from time and /to time.\n"
                                            + "Try: " + taskFormat);
                                }

                                if (hasFrom && !hasTo) {
                                    throw new TianyiException("An event with /from must also contain /to time.\n"
                                            + "Try: " + taskFormat);
                                }

                                if (!hasFrom && hasTo) {
                                    throw new TianyiException("An event with /to must also contain /from time.\n"
                                            + "Try: " + taskFormat);
                                }

                                if (inputParts[1].indexOf("/to") < inputParts[1].indexOf("/from")) {
                                    throw new TianyiException("/from time must appear before /to time .\n"
                                            + "Try: " + taskFormat);
                                }

                                String[] eventParts = inputParts[1].split("\\s+/from\\s+", 2);

                                if (eventParts.length < 2) {
                                    throw new TianyiException("An event must contain /from time.\n"
                                            + "Try: " + taskFormat);
                                }

                                String[] timeParts = eventParts[1].split("\\s+/to\\s+", 2);

                                if (timeParts.length < 2) {
                                    throw new TianyiException("An event must contain /to time.\n"
                                            + "Try: " + taskFormat);
                                }

                                task = new Event(eventParts[0], timeParts[0], timeParts[1]);
                                break;
                        }

                        tasks.add(task);
                        print("Got it. I've added this task:\n"
                                + "  " + task + "\n"
                                + "Now you have " + tasks.size() + " tasks in the list.");
                        break;

                    // mark or unmark a task
                    case "mark", "unmark", "delete":
                        if (inputParts.length < 2 || inputParts[1].isBlank()) {
                            throw new TianyiException("Please specify a task number.\n"
                                    + "Try: " + command + " 1");
                        }

                        if (tasks.isEmpty()) {
                            throw new TianyiException("There are no tasks in your list.\n"
                                    + "Please add a task.\n"
                                    + "Try: todo borrow book");
                        }

                        int taskNumber = 0;

                        try {
                            taskNumber = Integer.parseInt(inputParts[1]);
                        } catch (NumberFormatException e) {
                            throw new TianyiException(inputParts[1] + " is not a valid task number."
                                    + "Try: " + command + " 1");
                        }

                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            throw new TianyiException("Task number " + taskNumber + " does not exist.\n"
                                    + "Please enter a number from 1 to " + tasks.size() + "."
                                    + "Try: " + command + " 1");
                        }

                        switch (command) {
                            case "mark":
                                tasks.get(taskNumber - 1).markAsDone();
                                print("Nice! I've marked this task as done:\n"
                                        + "  " + tasks.get(taskNumber - 1));
                                break;
                            case "unmark":
                                tasks.get(taskNumber - 1).unmarkAsDone();
                                print("OK, I've marked this task as not done yet:\n"
                                        + "  " + tasks.get(taskNumber - 1));
                                break;
                            case "delete":
                                print("Noted. I've removed this task:\n"
                                        + "  " +  tasks.get(taskNumber - 1) + "\n"
                                        + "Now you have " + (tasks.size() - 1) + " tasks in the list.");
                                tasks.remove(taskNumber - 1);
                                break;
                        }

                        break;
                    default:
                        throw new TianyiException("I'm sorry, but I don't know what that means.");
                }
            } catch (TianyiException e) {
                print("Oops! " + e.getMessage());
            }
        }

        print(farewell);
    }
}
