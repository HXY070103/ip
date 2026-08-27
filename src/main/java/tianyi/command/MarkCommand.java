package tianyi.command;

import tianyi.TianyiException;
import tianyi.storage.Storage;
import tianyi.task.TaskList;
import tianyi.ui.Ui;

/**
 * Marks a task as completed.
 */
public class MarkCommand extends Command {
    private final int index;

    /**
     * Creates a command that marks a task at a zero-based index as completed.
     *
     * @param index zero-based index of the task to mark
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    /**
     * Marks the task, saves the updated list, and displays confirmation.
     *
     * @param tasks task list to update
     * @param ui user interface used to display confirmation
     * @param storage storage used to persist the updated list
     * @throws TianyiException if the updated task list cannot be saved
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws TianyiException {
        String response = tasks.markTask(index);
        storage.save(tasks.getTasks());
        ui.showResponse(response);
    }
}
