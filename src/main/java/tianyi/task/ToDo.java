package tianyi.task;

/**
 * Represents a task without a date or time.
 */
public class ToDo extends Task {
    /**
     * Creates a todo task with the specified description.
     */
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String getData() {
        return "T | " + super.getData();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
