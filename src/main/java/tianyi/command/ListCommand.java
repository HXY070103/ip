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

    public ListCommand(TaskTime date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        String response = date == null
                ? tasks.listTasks()
                : tasks.listTasks(date);
        ui.showResponse(response);
    }
}
