package tianyi.command;

import java.time.format.DateTimeParseException;

import tianyi.TianyiException;
import tianyi.task.Deadline;
import tianyi.task.Event;
import tianyi.task.Task;
import tianyi.task.TaskList;
import tianyi.task.TaskTime;
import tianyi.task.ToDo;

/**
 * Parses user input into commands with appropriately typed arguments.
 */
public class CommandParser {
    /**
     * Creates a parser for supported Tianyi commands.
     */
    public CommandParser() {
    }

    /**
     * Parses one line of user input into an executable command.
     *
     * @param input complete line entered by the user
     * @param tasks current task list, used to validate task indices
     * @return command represented by the input
     * @throws TianyiException if the command or any argument is invalid
     */
    public Command parse(String input, TaskList tasks) throws TianyiException {
        String[] inputParts = input.trim().split("\\s+", 2);

        CommandType type = CommandType.from(inputParts[0]);
        String argument = inputParts.length == 2
                ? inputParts[1].trim()
                : "";
        String example = type.getExample();

        switch (type) {
            case TODO, DEADLINE, EVENT:
                return new AddCommand(parseTask(type, argument, example));
            case MARK:
                return new MarkCommand(parseIndex(argument, example, tasks));
            case UNMARK:
                return new UnmarkCommand(parseIndex(argument, example, tasks));
            case DELETE:
                return new DeleteCommand(parseIndex(argument, example, tasks));
            case LIST:
                return new ListCommand(parseListDate(argument, example));
            case BYE:
                validateNoArgument(argument, example);
                return new ExitCommand();
            default:
                throw new TianyiException("I'm sorry, but I don't know what that means.");
        }
    }

    /**
     * Parses the argument of a task-creation command.
     */
    private Task parseTask(CommandType type, String argument, String example)
            throws TianyiException {
        if (argument.isBlank()) {
            throw new TianyiException("The argument of " + type + " command cannot be empty.\n"
                    + "Try: " + example);
        }

        switch (type) {
            case TODO:
                return new ToDo(argument);
            case DEADLINE:
                return createDeadline(argument, example);
            case EVENT:
                return createEvent(argument, example);
            default:
                throw new TianyiException("Command does not create a task: " + type);
        }
    }

    /**
     * Creates a deadline from its description and {@code /by} value.
     */
    private Task createDeadline(String argument, String example)
            throws TianyiException {
        String[] deadlineParts = argument.split("\\s*/by\\s*", 2);

        if (deadlineParts.length < 2) {
            throw new TianyiException("Deadline command must contain /by.\n"
                    + "Try: " + example);
        }

        String description = getRequiredPart(
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
            return new Deadline(description, new TaskTime(byDateTime));
        } catch (DateTimeParseException e) {
            throw new TianyiException("Invalid deadline date or time. "
                    + "Please use d-M-yyyy with optional HH:mm.\n"
                    + "Try: " + example);
        }
    }

    /**
     * Creates an event from its description, {@code /from}, and {@code /to} values.
     */
    private Task createEvent(String argument, String example)
            throws TianyiException {
        String[] eventParts = argument.split("\\s*/from\\s*", 2);

        if (eventParts.length < 2) {
            throw new TianyiException("Event command must contain /from.\n"
                    + "Try: " + example);
        }

        String description = getRequiredPart(
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
                        + "Try: " + example
        );
        String toTime = getRequiredPart(
                timeParts,
                1,
                "The to date of event command cannot be empty.\n"
                        + "Try: " + example
        );

        try {
            return new Event(description, new TaskTime(fromTime), new TaskTime(toTime));
        } catch (DateTimeParseException e) {
            throw new TianyiException("Invalid event date or time. "
                    + "Please use d-M-yyyy with optional HH:mm.\n"
                    + "Try: " + example);
        }
    }

    /**
     * Converts a user-facing task number into a validated zero-based index.
     */
    private int parseIndex(String argument, String example, TaskList tasks)
            throws TianyiException {
        if (argument.isBlank()) {
            throw new TianyiException("Please specify a task number.\n"
                    + "Try: " + example);
        }

        if (tasks.isEmpty()) {
            throw new TianyiException("There is no task in your list.\n"
                    + "Please add a task.\n"
                    + "Try: " + CommandType.TODO.getExample());
        }

        int taskNumber;

        try {
            taskNumber = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new TianyiException(argument + " is not a valid task number.\n"
                    + "Try: " + example);
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new TianyiException(
                    "Task number " + taskNumber + " does not exist.\n"
                            + "Please enter a number from 1 to " + tasks.size() + "."
                            + "Try: " + example);
        }

        return taskNumber - 1;
    }

    /**
     * Parses the optional date accepted by the list command.
     */
    private TaskTime parseListDate(String argument, String example)
            throws TianyiException {
        if (argument.isBlank()) {
            return null;
        }

        try {
            TaskTime time = new TaskTime(argument);

            if (time.hasTime()) {
                throw new TianyiException("Invalid list date.\n"
                        + "Please use d-M-yyyy.\n"
                        + "Try: " + example);
            }

            return time;
        } catch (DateTimeParseException e) {
            throw new TianyiException("Invalid list date.\n"
                    + "Please use d-M-yyyy.\n"
                    + "Try: " + example);
        }
    }

    /**
     * Rejects unexpected arguments for commands that accept none.
     */
    private void validateNoArgument(String argument, String example)
            throws TianyiException {
        if (!argument.isBlank()) {
            throw new TianyiException("Bye command does not accept any arguments.\n"
                    + "Try: " + example);
        }
    }

    /**
     * Extracts and validates one required component of a split argument.
     */
    private String getRequiredPart(String[] parts, int index, String errorMessage)
            throws TianyiException {
        if (index >= parts.length || parts[index].isBlank()) {
            throw new TianyiException(errorMessage);
        }

        return parts[index].trim();
    }
}
