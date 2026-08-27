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
     * Executes this command against the specified application components.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws TianyiException;

    /**
     * Returns whether this command ends the chatbot session.
     */
    public boolean isExit() {
        return false;
    }
}
