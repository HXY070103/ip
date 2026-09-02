package tianyi.command;

import tianyi.TianyiException;
import tianyi.storage.Storage;
import tianyi.task.Task;
import tianyi.task.TaskList;

/**
 * Adds a task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds the specified task.
     *
     * @param task Task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Adds the task, saves the updated list, and returns confirmation.
     *
     * @param tasks Task list to update.
     * @param storage Storage used to persist the updated list.
     * @return Confirmation describing the added task.
     * @throws TianyiException If the updated task list cannot be saved.
     */
    @Override
    public String execute(TaskList tasks, Storage storage)
            throws TianyiException {
        String response = tasks.addTask(task);
        storage.save(tasks.getTasks());
        return response;
    }
}
