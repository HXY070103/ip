package tianyi.command;

import tianyi.storage.Storage;
import tianyi.task.TaskList;
import tianyi.task.TaskTime;
import tianyi.ui.Ui;

/**
 * Lists all tasks or tasks occurring on a specified date.
 */
public class ListCommand extends Command {
    private final TaskTime date;

    /**
     * Creates a command that lists all tasks or those occurring on a date.
     *
     * @param date date used to filter tasks, or {@code null} to list all tasks
     */
    public ListCommand(TaskTime date) {
        this.date = date;
    }

    /**
     * Formats the requested tasks and displays them to the user.
     *
     * @param tasks task list to read
     * @param ui user interface used to display the tasks
     * @param storage storage instance, which is not used
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        String response = date == null
                ? tasks.listTasks()
                : tasks.listTasks(date);
        ui.showResponse(response);
    }
}
