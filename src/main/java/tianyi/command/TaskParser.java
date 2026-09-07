package tianyi.command;

import java.time.format.DateTimeParseException;

import tianyi.TianyiException;
import tianyi.task.Deadline;
import tianyi.task.Event;
import tianyi.task.Task;
import tianyi.task.TaskTime;
import tianyi.task.ToDo;

/**
 * Parses task-creation command arguments into tasks.
 */
class TaskParser {
    private static final int PART_INDEX_FIRST = 0;
    private static final int PART_INDEX_SECOND = 1;
    private static final int PART_COUNT_EXPECTED = 2;

    /**
     * Parses the argument of a task-creation command.
     *
     * @param type Type of task to create.
     * @param argument Task details supplied by the user.
     * @return Task represented by the command argument.
     * @throws TianyiException If the command does not create a task or its argument is invalid.
     */
    public Task parse(CommandType type, String argument)
            throws TianyiException {
        String example = type.getExample();

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
        String[] deadlineParts = argument.split("\\s*/by\\s*", PART_COUNT_EXPECTED);

        if (deadlineParts.length < PART_COUNT_EXPECTED) {
            throw new TianyiException("Deadline command must contain /by.\n"
                    + "Try: " + example);
        }

        String description = getRequiredPart(
                deadlineParts,
                PART_INDEX_FIRST,
                "The description of deadline command cannot be empty.\n"
                        + "Try: " + example
        );
        String deadline = getRequiredPart(
                deadlineParts,
                PART_INDEX_SECOND,
                "The by date of deadline command cannot be empty.\n"
                        + "Try: " + example
        );

        TaskTime deadlineTime = parseTaskTime(deadline, CommandType.DEADLINE);
        return new Deadline(description, deadlineTime);
    }

    /**
     * Creates an event from its description, {@code /from}, and {@code /to} values.
     */
    private Task createEvent(String argument, String example)
            throws TianyiException {
        String[] eventParts = argument.split("\\s*/from\\s*", PART_COUNT_EXPECTED);

        if (eventParts.length < PART_COUNT_EXPECTED) {
            throw new TianyiException("Event command must contain /from.\n"
                    + "Try: " + example);
        }

        String description = getRequiredPart(
                eventParts,
                PART_INDEX_FIRST,
                "The description of event command cannot be empty.\n"
                        + "Try: " + example
        );
        String timeRange = getRequiredPart(
                eventParts,
                PART_INDEX_SECOND,
                "Event command must contain /to.\n"
                        + "Try: " + example
        );

        String[] timeParts = timeRange.split("\\s*/to\\s*", PART_COUNT_EXPECTED);

        if (timeParts.length < PART_COUNT_EXPECTED) {
            throw new TianyiException("Event command must contain /to.\n"
                    + "Try: " + example);
        }

        String fromTime = getRequiredPart(
                timeParts,
                PART_INDEX_FIRST,
                "The from date of event command cannot be empty.\n"
                        + "Try: " + example
        );
        String toTime = getRequiredPart(
                timeParts,
                PART_INDEX_SECOND,
                "The to date of event command cannot be empty.\n"
                        + "Try: " + example
        );

        TaskTime startTime = parseTaskTime(fromTime, CommandType.EVENT);
        TaskTime endTime = parseTaskTime(toTime, CommandType.EVENT);
        return new Event(description, startTime, endTime);
    }

    /**
     * Parses a task date and converts format errors into command-specific errors.
     *
     * @param input Date and optional time supplied by the user.
     * @param type Command type that supplies error context and example usage.
     * @return Parsed task date and optional time.
     * @throws TianyiException If the date or time has an invalid format.
     */
    private TaskTime parseTaskTime(String input, CommandType type)
            throws TianyiException {
        try {
            return new TaskTime(input);
        } catch (DateTimeParseException e) {
            throw new TianyiException("Invalid " + type + " date or time. "
                    + "Please use d-M-yyyy with optional HH:mm.\n"
                    + "Try: " + type.getExample());
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
