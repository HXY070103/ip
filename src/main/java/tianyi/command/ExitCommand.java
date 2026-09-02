package tianyi.command;

import tianyi.storage.Storage;
import tianyi.task.TaskList;

/**
 * Ends the chatbot session.
 */
public class ExitCommand extends Command {
    private static final String FAREWELL = "Bye. Hope to see you again soon!";

    /**
     * Creates a command that ends the current application session.
     */
    public ExitCommand() {
    }

    /**
     * Returns the farewell message without changing stored tasks.
     *
     * @param tasks Task list, which is not modified.
     * @param storage Storage instance, which is not used.
     * @return Farewell message for the user.
     */
    @Override
    public String execute(TaskList tasks, Storage storage) {
        return FAREWELL;
    }

    /**
     * Reports that executing this command should end the application session.
     *
     * @return Always {@code true}.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
