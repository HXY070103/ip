import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Runs the Tianyi chatbot application.
 */
public class Tianyi {
    private static final File DATA_FILE = new File("Data", "tianyi.txt");

    public static void print(String content) {
        System.out.println("____________________________________________________________\n"
                + content + "\n"
                + "____________________________________________________________");
    }

    public static void printTasks(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            print("No tasks found.");
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

    private static void saveTasks(List<Task> tasks) throws IOException {
        File dataFolder = DATA_FILE.getParentFile();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            throw new IOException("Unable to create the data folder.");
        }

        try (FileWriter fw = new FileWriter(DATA_FILE)) {
            for (Task task : tasks) {
                fw.write(task.getData() + System.lineSeparator());
            }
        }
    }

    private static Task parseTask(String data) throws TianyiException {
        String[] dataParts = data.split("\\s*\\|\\s*");

        if (dataParts.length < 3) {
            throw new TianyiException("Invalid task data: " + data);
        }

        String type = dataParts[0];
        String status = dataParts[1];
        String description = dataParts[2];

        Task task;

        switch (type) {
            case "T":
                if (dataParts.length != 3) {
                    throw new TianyiException("Invalid todo data: " + data);
                }

                task = new ToDo(description);
                break;

            case "D":
                if (dataParts.length != 4) {
                    throw new TianyiException("Invalid deadline data: " + data);
                }

                try {
                    TaskTime deadline = new TaskTime(dataParts[3]);
                    task = new Deadline(description, deadline);
                } catch (DateTimeParseException e) {
                    throw new TianyiException("Invalid date and time in deadline data: " + dataParts[3]);
                }
                break;

            case "E":
                if (dataParts.length != 5) {
                    throw new TianyiException("Invalid event data: " + data);
                }

                try {
                    TaskTime startTime = new TaskTime(dataParts[3]);
                    TaskTime endTime = new TaskTime(dataParts[4]);
                    task = new Event(description, startTime, endTime);
                } catch (DateTimeParseException e) {
                    throw new TianyiException("Invalid date and time in event data: " + data);
                }
                break;

            default:
                throw new TianyiException("Unknown task type: " + type);
        }

        if (status.equals("1")) {
            task.markAsDone();
        } else if (!status.equals("0")) {
            throw new TianyiException("Invalid task status: " + status);
        }

        return task;
    }

    private static List<Task> loadTasks() throws IOException {
        List<Task> tasks = new ArrayList<>();

        if (!DATA_FILE.exists()) {
            return tasks;
        }

        try (Scanner sc = new Scanner(DATA_FILE)) {
            while (sc.hasNextLine()) {
                try {
                    Task task = parseTask(sc.nextLine());
                    tasks.add(task);
                } catch (TianyiException e) {
                    print("Oops! " + e.getMessage());
                }
            }
        }

        return tasks;
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

        try {
            tasks = loadTasks();
        } catch (IOException e) {
            System.out.println("File not found");
        }

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
                                String byDateTime = getRequiredPart(
                                        deadlineParts,
                                        1,
                                        "The by date of deadline command cannot be empty.\n"
                                                + "Try: " + example
                                    );

                                try {
                                    TaskTime deadline = new TaskTime(byDateTime);
                                    task = new Deadline(deadlineDescription, deadline);
                                } catch (DateTimeParseException e) {
                                    throw new TianyiException(
                                            "Invalid deadline date or time. "
                                                    + "Please use d-M-yyyy with optional HH:mm.\n"
                                                    + "Try: " + example);
                                }
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
                                        "The from date of event command cannot be empty.\n"
                                                +  "Try: " + example
                                    );
                                String toTime = getRequiredPart(
                                        timeParts,
                                        1,
                                        "The to date of event command cannot be empty.\n"
                                                +  "Try: " + example
                                );

                                try {
                                    TaskTime startTime = new TaskTime(fromTime);
                                    TaskTime endTime = new TaskTime(toTime);
                                    task = new Event(eventDescription, startTime, endTime);
                                } catch (DateTimeParseException e) {
                                    throw new TianyiException(
                                            "Invalid event date or time. "
                                                    + "Please use d-M-yyyy with optional HH:mm.\n"
                                                    + "Try: " + example);
                                }
                                break;
                        }

                        tasks.add(task);

                        try {
                            saveTasks(tasks);
                        } catch (IOException e) {
                            System.out.println("File not found");
                        }

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

                        try {
                            saveTasks(tasks);
                        } catch (IOException e) {
                            System.out.println("File not found");
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
