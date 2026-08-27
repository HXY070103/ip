package tianyi.task;

/**
 * Represents a task that must be completed by a specific date and optional time.
 */
public class Deadline extends Task {
    private final TaskTime deadline;

    /**
     * Creates a deadline with the specified description and due date.
     */
    public Deadline(String description, TaskTime deadline) {
        super(description);

        this.deadline = deadline;
    }

    @Override
    public boolean isOccurringOn(TaskTime taskTime) {
        return !deadline.isBefore(taskTime);
    }

    @Override
    public String getData() {
        return "D | " + super.getData() + " | " + deadline.getData();
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + deadline + ")";
    }
}
