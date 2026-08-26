/**
 * Represents an executable command issued by the user.
 */
public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws TianyiException;

    public boolean isExit() {
        return false;
    }
}
