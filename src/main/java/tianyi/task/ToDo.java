package tianyi.task;

/**
 * Represents a task without an associated date or time.
 */
public class ToDo extends Task {
    /**
     * Creates an incomplete todo with the specified description.
     *
     * @param description Description of the todo.
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Serializes this todo for storage.
     *
     * @return Storage representation prefixed with the todo type marker.
     */
    @Override
    public String getData() {
        return "T | " + super.getData();
    }

    /**
     * Formats this todo for display.
     *
     * @return User-facing representation prefixed with the todo type marker.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
