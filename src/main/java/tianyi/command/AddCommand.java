package tianyi.command;

import tianyi.TianyiException;
import tianyi.storage.Storage;
import tianyi.task.Task;
import tianyi.task.TaskList;
import tianyi.ui.Ui;

/**
 * Adds a task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds the specified task.
     *
     * @param task task to add
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Adds the task, saves the updated list, and displays confirmation.
     *
     * @param tasks task list to update
     * @param ui user interface used to display confirmation
     * @param storage storage used to persist the updated list
     * @throws TianyiException if the updated task list cannot be saved
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws TianyiException {
        String response = tasks.addTask(task);
        storage.save(tasks.getTasks());
        ui.showResponse(response);
    }
}
