package tianyi.command;

import java.util.Locale;

import tianyi.TianyiException;

/**
 * Represents a supported command type, keyword, and example usage.
 */
public enum CommandType {
    /** Ends the application session. */
    BYE("bye", "bye"),
    /** Lists all tasks or dated tasks occurring on a specified date. */
    LIST("list", "list 2-12-2019"),
    /** Adds a todo task. */
    TODO("todo", "todo borrow book"),
    /** Adds a task with a deadline. */
    DEADLINE("deadline", "deadline return book /by 2-12-2019 18:00"),
    /** Adds an event with a start and end. */
    EVENT("event", "event meeting /from 2-12-2019 14:00 /to 2-12-2019 16:00"),
    /** Marks a task as completed. */
    MARK("mark", "mark 1"),
    /** Marks a task as incomplete. */
    UNMARK("unmark", "unmark 1"),
    /** Removes a task from the task list. */
    DELETE("delete", "delete 1");

    private final String keyword;
    private final String example;

    CommandType(String keyword, String example) {
        this.keyword = keyword;
        this.example = example;
    }

    /**
     * Resolves a command keyword without regard to letter case.
     *
     * @param keyword Command keyword entered by the user.
     * @return Command type matching the keyword.
     * @throws TianyiException If the keyword is not supported.
     */
    public static CommandType from(String keyword)
            throws TianyiException {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);

        for (CommandType type : values()) {
            if (type.keyword.equals(normalizedKeyword)) {
                return type;
            }
        }

        throw new TianyiException("I'm sorry, but I don't know what that means.");
    }

    /**
     * Returns an example showing valid usage of this command type.
     *
     * @return Example command input.
     */
    public String getExample() {
        return example;
    }

    /**
     * Returns the input keyword for this command type.
     *
     * @return Lowercase command keyword.
     */
    @Override
    public String toString() {
        return keyword;
    }
}
