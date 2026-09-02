package tianyi.command;

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
    public String execute(TaskList tasks, Storage storage) {
        return date == null
                ? tasks.listTasks()
                : tasks.listTasks(date);
    }
}
