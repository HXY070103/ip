/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void unmarkAsDone() {
        isDone = false;
    }

    /**
     * Returns whether this task should appear in a list for the specified date.
     * Tasks without dates do not occur on any date by default.
     *
     * @param taskTime date being queried
     * @return true if this task occurs on the date
     */
    public boolean isOccurringOn(TaskTime taskTime) {
        return false;
    }

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
