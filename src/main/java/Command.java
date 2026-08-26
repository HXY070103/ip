import java.util.Locale;

/**
 * Represents a command supported by Tianyi and its example usage.
 */
public enum Command {
    BYE("bye", "bye"),
    LIST("list", "list"),
    TODO("todo", "todo borrow book"),
    DEADLINE("deadline", "deadline return book /by 2-12-2019 18:00"),
    EVENT("event", "event meeting /from 2-12-2019 14:00 /to 2-12-2019 16:00"),
    MARK("mark", "mark 1"),
    UNMARK("unmark", "unmark 1"),
    DELETE("delete", "delete 1");

    private final String keyword;
    private final String example;

    Command(String keyword, String example) {
        this.keyword = keyword;
        this.example = example;
    }

    public static Command from(String keyword) throws TianyiException {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);

        for (Command command : values()) {
            if (command.keyword.equals(normalizedKeyword)) {
                return command;
            }
        }

        throw new TianyiException("I'm sorry, but I don't know what that means.");
    }

    public String getExample() {
        return this.example;
    }

    @Override
    public String toString() {
        return this.keyword;
    }
}
