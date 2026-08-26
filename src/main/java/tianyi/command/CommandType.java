package tianyi.command;

import java.util.Locale;

import tianyi.TianyiException;

/**
 * Represents a supported command type, keyword, and example usage.
 */
public enum CommandType {
    BYE("bye", "bye"),
    LIST("list", "list 2-12-2019"),
    TODO("todo", "todo borrow book"),
    DEADLINE("deadline", "deadline return book /by 2-12-2019 18:00"),
    EVENT("event", "event meeting /from 2-12-2019 14:00 /to 2-12-2019 16:00"),
    MARK("mark", "mark 1"),
    UNMARK("unmark", "unmark 1"),
    DELETE("delete", "delete 1");

    private final String keyword;
    private final String example;

    CommandType(String keyword, String example) {
        this.keyword = keyword;
        this.example = example;
    }

    public static CommandType from(String keyword) throws TianyiException {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);

        for (CommandType type : values()) {
            if (type.keyword.equals(normalizedKeyword)) {
                return type;
            }
        }

        throw new TianyiException("I'm sorry, but I don't know what that means.");
    }

    public String getExample() {
        return example;
    }

    @Override
    public String toString() {
        return keyword;
    }
}
