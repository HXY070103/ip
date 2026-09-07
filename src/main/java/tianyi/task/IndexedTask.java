package tianyi.task;

/**
 * Associates a task with its current one-based number in a task list.
 */
final class IndexedTask {
    private final int taskNumber;
    private final Task task;

    /**
     * Creates an indexed task for display in list and find results.
     *
     * @param taskNumber One-based task number in the complete task list.
     * @param task Task associated with the number.
     */
    IndexedTask(int taskNumber, Task task) {
        this.taskNumber = taskNumber;
        this.task = task;
    }

    Task getTask() {
        return task;
    }

    @Override
    public String toString() {
        return taskNumber + "." + task;
    }
}
