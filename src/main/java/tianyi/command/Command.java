package tianyi.command;

import tianyi.TianyiException;
import tianyi.storage.Storage;
import tianyi.task.TaskList;
import tianyi.ui.Ui;

/**
 * Represents an executable command issued by the user.
 */
public abstract class Command {
    /**
     * Creates a command for execution by the application.
     */
    protected Command() {
    }

    /**
     * Applies this command to the task list and displays its result.
     *
     * @param tasks task list on which the command operates
     * @param ui user interface used to display feedback
     * @param storage storage used to persist task changes
     * @throws TianyiException if the command cannot be completed
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws TianyiException;

    /**
     * Reports whether this command should end the application session.
     *
     * @return {@code true} if the application should exit, otherwise {@code false}
     */
    public boolean isExit() {
        return false;
    }
}
