package tianyi.task;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the specified description.
     *
     * @param description Description of the task.
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
     * Reports whether this task occurs on the specified date.
     * Plain tasks have no date and therefore never occur on a specific date.
     *
     * @param taskTime Date to check.
     * @return Always {@code false} for a plain task.
     */
    public boolean isOccurringOn(TaskTime taskTime) {
        return false;
    }

    /**
     * Reports whether this task's description contains the specified keyword.
     *
     * @param keyword Keyword to find in the description.
     * @return {@code true} if the description contains the keyword, otherwise {@code false}.
     */
    public boolean matchesKeyword(String keyword) {
        return description.contains(keyword);
    }

    /**
     * Serializes the task's completion status and description.
     *
     * @return Storage representation of this task's common fields.
     */
    public String getData() {
        int status = isDone ? 1 : 0;
        return status + " | " + description;
    }

    /**
     * Formats the task's completion status and description for display.
     *
     * @return User-facing representation of the task.
     */
    @Override
    public String toString() {
        String statusIcon = isDone ? "[X]" : "[ ]";
        return statusIcon + " " + description;
    }
}
