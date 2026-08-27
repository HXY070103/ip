package tianyi.task;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the specified description.
     */
    public Task(String description) {
        this.description = description;
        isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void unmarkAsDone() {
        isDone = false;
    }

    /**
     * Returns whether this task occurs on the specified date.
     */
    public boolean isOccurringOn(TaskTime taskTime) {
        return false;
    }

    /**
     * Returns the persistent representation of this task.
     */
    public String getData() {
        int status = isDone ? 1 : 0;
        return status + " | " + description;
    }

    @Override
    public String toString() {
        String statusIcon = isDone ? "[X]" : "[ ]";
        return statusIcon + " " + description;
    }
}
