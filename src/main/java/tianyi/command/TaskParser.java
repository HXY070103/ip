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
        if (argument.isBlank()) {
            throw new TianyiException("The argument of " + type + " cannot be empty.\n"
                    + "Try: " + type.getExample());
        }

        return switch (type) {
            case TODO -> new ToDo(argument);
            case DEADLINE -> createDeadline(type, argument);
            case EVENT -> createEvent(type, argument);
            default -> throw new TianyiException(type + " does not create a task.");
        };
    }

    /**
     * Creates a deadline from its description and {@code /by} value.
     *
     * @param type Command type that supplies error context and example usage.
     * @param argument Deadline details supplied by the user.
     * @return Deadline represented by the command argument.
     * @throws TianyiException If the deadline details are incomplete or invalid.
     */
    private Task createDeadline(CommandType type, String argument)
            throws TianyiException {
        String[] deadlineParts = argument.split("\\s*/by\\s*", PART_COUNT_EXPECTED);

        if (deadlineParts.length < PART_COUNT_EXPECTED) {
            throw new TianyiException(type + " must contain /by.\n"
                    + "Try: " + type.getExample());
        }

        String description = getRequiredPart(
                deadlineParts,
                PART_INDEX_FIRST,
                "The description of " + type + " cannot be empty.\n"
                        + "Try: " + type.getExample()
        );
        String deadline = getRequiredPart(
                deadlineParts,
                PART_INDEX_SECOND,
                "The by date of " + type + " cannot be empty.\n"
                        + "Try: " + type.getExample()
        );

        TaskTime deadlineTime = parseTaskTime(type, deadline);
        return new Deadline(description, deadlineTime);
    }

    /**
     * Creates an event from its description, {@code /from}, and {@code /to} values.
     *
     * @param type Command type that supplies error context and example usage.
     * @param argument Event details supplied by the user.
     * @return Event represented by the command argument.
     * @throws TianyiException If the event details are incomplete or invalid.
     */
    private Task createEvent(CommandType type, String argument)
            throws TianyiException {
        String[] eventParts = argument.split("\\s*/from\\s*", PART_COUNT_EXPECTED);

        if (eventParts.length < PART_COUNT_EXPECTED) {
            throw new TianyiException(type + " must contain /from.\n"
                    + "Try: " + type.getExample());
        }

        String description = getRequiredPart(
                eventParts,
                PART_INDEX_FIRST,
                "The description of " + type + " cannot be empty.\n"
                        + "Try: " + type.getExample()
        );
        String timeRange = getRequiredPart(
                eventParts,
                PART_INDEX_SECOND,
                type + " must contain /to.\n"
                        + "Try: " + type.getExample()
        );

        String[] timeParts = timeRange.split("\\s*/to\\s*", PART_COUNT_EXPECTED);

        if (timeParts.length < PART_COUNT_EXPECTED) {
            throw new TianyiException(type + " must contain /to.\n"
                    + "Try: " + type.getExample());
        }

        String fromTime = getRequiredPart(
                timeParts,
                PART_INDEX_FIRST,
                "The from date of " + type + " cannot be empty.\n"
                        + "Try: " + type.getExample()
        );
        String toTime = getRequiredPart(
                timeParts,
                PART_INDEX_SECOND,
                "The to date of " + type + " cannot be empty.\n"
                        + "Try: " + type.getExample()
        );

        TaskTime startTime = parseTaskTime(type, fromTime);
        TaskTime endTime = parseTaskTime(type, toTime);
        return new Event(description, startTime, endTime);
    }

    /**
     * Parses a task date and converts format errors into command-specific errors.
     *
     * @param type Command type that supplies error context and example usage.
     * @param input Date and optional time supplied by the user.
     * @return Parsed task date and optional time.
     * @throws TianyiException If the date or time has an invalid format.
     */
    private TaskTime parseTaskTime(CommandType type, String input)
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
     *
     * @param parts Argument components to inspect.
     * @param index Index of the required component.
     * @param errorMessage Message to report if the component is absent or blank.
     * @return Trimmed required component.
     * @throws TianyiException If the required component is absent or blank.
     */
    private String getRequiredPart(String[] parts, int index, String errorMessage)
            throws TianyiException {
        if (index >= parts.length || parts[index].isBlank()) {
            throw new TianyiException(errorMessage);
        }

        return parts[index].trim();
    }
}
