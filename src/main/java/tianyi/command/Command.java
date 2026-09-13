package tianyi.command;

import java.util.List;
import java.util.stream.Collectors;

import tianyi.Response;
import tianyi.TianyiException;
import tianyi.storage.Storage;
import tianyi.task.IndexedTask;
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
    public abstract Response execute(TaskList tasks, Storage storage)
            throws TianyiException;

    /**
     * Creates a reply from numbered query results, omitting the header for empty results.
     *
     * @param header Title describing the query.
     * @param indexedTasks Matching tasks with their original list numbers.
     * @return Structured query response.
     */
    protected Response createListResponse(String header, List<IndexedTask> indexedTasks) {
        if (indexedTasks.isEmpty()) {
            return new Response("", "No tasks found.");
        }

        String message = indexedTasks.stream()
                .map(IndexedTask::toString)
                .collect(Collectors.joining("\n"));

        return new Response(header, message);
    }
}
