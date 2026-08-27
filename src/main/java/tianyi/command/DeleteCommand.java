package tianyi.command;

import tianyi.TianyiException;
import tianyi.storage.Storage;
import tianyi.task.TaskList;
import tianyi.ui.Ui;

/**
 * Deletes a task from the task list.
 */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Creates a command that deletes a task at a zero-based index.
     *
     * @param index Zero-based index of the task to delete.
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Deletes the task, saves the updated list, and displays confirmation.
     *
     * @param tasks Task list to update.
     * @param ui User interface used to display confirmation.
     * @param storage Storage used to persist the updated list.
     * @throws TianyiException If the updated task list cannot be saved.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws TianyiException {
        String response = tasks.deleteTask(index);
        storage.save(tasks.getTasks());
        ui.showResponse(response);
    }
}
