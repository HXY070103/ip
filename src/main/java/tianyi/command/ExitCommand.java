package tianyi.command;

import tianyi.storage.Storage;
import tianyi.task.TaskList;
import tianyi.ui.Ui;

/**
 * Ends the chatbot session.
 */
public class ExitCommand extends Command {
    /**
     * Creates a command that ends the current application session.
     */
    public ExitCommand() {
    }

    /**
     * Displays the farewell message without changing stored tasks.
     *
     * @param tasks task list, which is not modified
     * @param ui user interface used to display the farewell message
     * @param storage storage instance, which is not used
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /**
     * Reports that executing this command should end the application session.
     *
     * @return always {@code true}
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
