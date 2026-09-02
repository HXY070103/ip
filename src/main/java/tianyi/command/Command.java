package tianyi.command;

import tianyi.TianyiException;
import tianyi.storage.Storage;
import tianyi.task.TaskList;

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
     * Applies this command to the task list and returns its result.
     *
     * @param tasks Task list on which the command operates.
     * @param storage Storage used to persist task changes.
     * @return Response describing the command result.
     * @throws TianyiException If the command cannot be completed.
     */
    public abstract String execute(TaskList tasks, Storage storage)
            throws TianyiException;

    /**
     * Reports whether this command should end the application session.
     *
     * @return {@code true} if the application should exit, otherwise {@code false}.
     */
    public boolean isExit() {
        return false;
    }
}
