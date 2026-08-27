package tianyi.task;

/**
 * Represents a task that must be completed by a specific date and optional time.
 */
public class Deadline extends Task {
    private final TaskTime deadline;

    /**
     * Creates an incomplete deadline with its due date and optional time.
     *
     * @param description description of the deadline
     * @param deadline date and optional time by which the task is due
     */
    public Deadline(String description, TaskTime deadline) {
        super(description);

        this.deadline = deadline;
    }

    /**
     * Reports whether this deadline remains due on the specified date.
     *
     * @param taskTime date to check
     * @return {@code true} if the deadline is on or after the specified date
     */
    @Override
    public boolean isOccurringOn(TaskTime taskTime) {
        return !deadline.isBefore(taskTime);
    }

    /**
     * Serializes this deadline for storage.
     *
     * @return storage representation including the deadline type and due date
     */
    @Override
    public String getData() {
        return "D | " + super.getData() + " | " + deadline.getData();
    }

    /**
     * Formats this deadline for display.
     *
     * @return user-facing representation including its due date and optional time
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + deadline + ")";
    }
}
