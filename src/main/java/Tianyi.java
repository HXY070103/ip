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

        return inputParts[1].trim();
    }

    private static String getRequiredPart(String[] argumentParts, int index, String errorMessage)
            throws TianyiException {
        if (index >= argumentParts.length || argumentParts[index].isBlank()) {
            throw new TianyiException(errorMessage);
        }

        return argumentParts[index].trim();
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
                Command command = Command.from(inputParts[0]);
                String example = command.getExample();

                if (command == Command.BYE) {
                    if (inputParts.length > 1 && !inputParts[1].isBlank()) {
                        throw new TianyiException("Bye command does not accept any arguments.\n"
                                + "Try: " + example);
                    }

                    break;
                }

                switch (command) {
                    // Command
                    case LIST:
                        if (inputParts.length > 1 && !inputParts[1].isBlank()) {
                            throw new TianyiException("List command does not accept any arguments.\n"
                                    + "Try: " + example);
                        }

                        printTasks(tasks);
                        break;

                    // Command + Argument
                    case TODO, DEADLINE, EVENT:
                        String argument = getArgument(
                                inputParts,
                                "The argument of " + command + " command cannot be empty.\n"
                                        + "Try: " + example
                            );

                        Task task = null;

                        switch (command) {
                            case TODO:
                                task = new ToDo(argument);
                                break;
                            case DEADLINE:
                                String[] deadlineParts = argument.split("\\s*/by\\s*", 2);

                                if (deadlineParts.length < 2) {
                                    throw new TianyiException("Deadline command must contain /by.\n"
                                            + "Try: " + example);
                                }

                                String deadlineDescription = getRequiredPart(
                                        deadlineParts,
                                        0,
                                        "The description of deadline command cannot be empty.\n"
                                                + "Try: " + example
                                    );
                                String byTime = getRequiredPart(
                                        deadlineParts,
                                        1,
                                        "The by time of deadline command cannot be empty.\n"
                                                + "Try: " + example
                                    );

                                task = new Deadline(deadlineDescription, byTime);
                                break;
                            case EVENT:
                                String[] eventParts = argument.split("\\s*/from\\s*", 2);

                                if (eventParts.length < 2) {
                                    throw new TianyiException("Event command must contain /from.\n"
                                            + "Try: " + example);
                                }

                                String eventDescription = getRequiredPart(
                                        eventParts,
                                        0,
                                        "The description of event command cannot be empty.\n"
                                                + "Try: " + example
                                    );
                                String fromAndToTime = getRequiredPart(
                                        eventParts,
                                        1,
                                        "Event command must contain /to.\n"
                                                + "Try: " + example
                                    );

                                String[] timeParts = fromAndToTime.split("\\s*/to\\s*", 2);

                                if (timeParts.length < 2) {
                                    throw new TianyiException("Event command must contain /to.\n"
                                            + "Try: " + example);
                                }

                                String fromTime = getRequiredPart(
                                        timeParts,
                                        0,
                                        "The from time of event command cannot be empty.\n"
                                                +  "Try: " + example
                                    );
                                String toTime = getRequiredPart(
                                        timeParts,
                                        1,
                                        "The to time of event command cannot be empty.\n"
                                                +  "Try: " + example
                                );

                                task = new Event(eventDescription, fromTime, toTime);
                                break;
                        }

                        tasks.add(task);
                        print("Got it. I've added this task:\n"
                                + "  " + task + "\n"
                                + "Now you have " + tasks.size() + " tasks in the list.");
                        break;

                    // Command + Number
                    case MARK, UNMARK, DELETE:
                        if (inputParts.length < 2 || inputParts[1].isBlank()) {
                            throw new TianyiException("Please specify a task number.\n"
                                    + "Try: " + example);
                        }

                        if (tasks.isEmpty()) {
                            throw new TianyiException("There is no task in your list.\n"
                                    + "Please add a task.\n"
                                    + "Try: " + Command.TODO.getExample());
                        }

                        int taskNumber = 0;

                        try {
                            taskNumber = Integer.parseInt(inputParts[1]);
                        } catch (NumberFormatException e) {
                            throw new TianyiException(inputParts[1] + " is not a valid task number."
                                    + "Try: " + example);
                        }

                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            throw new TianyiException("Task number " + taskNumber + " does not exist.\n"
                                    + "Please enter a number from 1 to " + tasks.size() + "."
                                    + "Try: " + example);
                        }

                        switch (command) {
                            case MARK:
                                tasks.get(taskNumber - 1).markAsDone();
                                print("Nice! I've marked this task as done:\n"
                                        + "  " + tasks.get(taskNumber - 1));
                                break;
                            case UNMARK:
                                tasks.get(taskNumber - 1).unmarkAsDone();
                                print("OK, I've marked this task as not done yet:\n"
                                        + "  " + tasks.get(taskNumber - 1));
                                break;
                            case DELETE:
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
