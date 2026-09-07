package tianyi.command;

import java.time.format.DateTimeParseException;

import tianyi.TianyiException;
import tianyi.task.TaskList;
import tianyi.task.TaskTime;

/**
 * Parses user input into commands with appropriately typed arguments.
 */
public class CommandParser {
    private static final int INPUT_PART_INDEX_COMMAND = 0;
    private static final int INPUT_PART_INDEX_ARGUMENT = 1;
    private static final int INPUT_PART_COUNT_EXPECTED = 2;

    private static final int TASK_NUMBER_FIRST = 1;

    private final TaskParser taskParser;

    /**
     * Creates a parser for supported Tianyi commands.
     */
    public CommandParser() {
        taskParser = new TaskParser();
    }

    /**
     * Parses one line of user input into an executable command.
     *
     * @param input Complete line entered by the user.
     * @param tasks Current task list, used to validate task indices.
     * @return Command represented by the input.
     * @throws TianyiException If the command or any argument is invalid.
     */
    public Command parse(String input, TaskList tasks)
            throws TianyiException {
        String[] inputParts = input.trim().split("\\s+", INPUT_PART_COUNT_EXPECTED);

        CommandType type = CommandType.from(inputParts[INPUT_PART_INDEX_COMMAND]);
        String argument = inputParts.length == INPUT_PART_COUNT_EXPECTED
                ? inputParts[INPUT_PART_INDEX_ARGUMENT].trim()
                : "";
        String example = type.getExample();

        return switch (type) {
            case TODO, DEADLINE, EVENT -> new AddCommand(taskParser.parse(type, argument));
            case MARK -> new MarkCommand(parseIndex(argument, example, tasks));
            case UNMARK -> new UnmarkCommand(parseIndex(argument, example, tasks));
            case DELETE -> new DeleteCommand(parseIndex(argument, example, tasks));
            case LIST -> new ListCommand(parseListDate(argument, example));
            case FIND -> new FindCommand(parseFindKeyword(argument, example));
            case BYE -> {
                validateNoArgument(argument, example);
                yield new ExitCommand();
            }
        };
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

        if (taskNumber < TASK_NUMBER_FIRST || taskNumber > tasks.size()) {
            throw new TianyiException(
                    "Task number " + taskNumber + " does not exist.\n"
                            + "Please enter a number from " + TASK_NUMBER_FIRST + " to " + tasks.size() + ".\n"
                            + "Try: " + example);
        }

        return taskNumber - TASK_NUMBER_FIRST;
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

    private String parseFindKeyword(String argument, String example)
            throws TianyiException {
        if (argument.isBlank()) {
            throw new TianyiException(
                    "The keyword of find command cannot be empty.\n"
                            + "Try: " + example
            );
        }

        return argument;
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
}
