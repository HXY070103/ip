package tianyi.command;

import tianyi.Response;
import tianyi.storage.Storage;
import tianyi.task.TaskList;
import tianyi.task.TaskTime;

/**
 * Lists all tasks or tasks occurring on a specified date.
 */
public class ListCommand extends Command {
    private final TaskTime date;

    /**
     * Creates a command that lists all tasks or those occurring on a date.
     *
     * @param date Date used to filter tasks, or {@code null} to list all tasks.
     */
    public ListCommand(TaskTime date) {
        this.date = date;
    }

    /**
     * Formats and returns the requested tasks.
     *
     * @param tasks Task list to read.
     * @param storage Storage instance, which is not used.
     * @return Formatted tasks matching the command.
     */
    @Override
    public Response execute(TaskList tasks, Storage storage) {
        if (date == null) {
            return createListResponse("Here are the tasks in your list:", tasks.listTasks());
        }

        return createListResponse(
                "Here are deadlines/events occurring on " + date.getData() + ":",
                tasks.listTasks(date)
        );
    }
}
