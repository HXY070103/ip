package tianyi.task;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private final String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        isDone = false;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void unmarkAsDone() {
        isDone = false;
    }

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
