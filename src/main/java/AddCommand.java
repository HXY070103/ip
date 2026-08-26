/**
 * Adds a task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws TianyiException {
        String response = tasks.addTask(task);
        storage.save(tasks.getTasks());
        ui.showResponse(response);
    }
}
