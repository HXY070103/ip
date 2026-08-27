package tianyi.task;

/**
 * Represents a task without an associated date or time.
 */
public class ToDo extends Task {
    /**
     * Creates an incomplete todo with the specified description.
     *
     * @param description description of the todo
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Serializes this todo for storage.
     *
     * @return storage representation prefixed with the todo type marker
     */
    @Override
    public String getData() {
        return "T | " + super.getData();
    }

    /**
     * Formats this todo for display.
     *
     * @return user-facing representation prefixed with the todo type marker
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
