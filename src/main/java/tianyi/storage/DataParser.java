package tianyi.storage;

import java.time.format.DateTimeParseException;

import tianyi.task.Deadline;
import tianyi.task.Event;
import tianyi.task.Task;
import tianyi.task.TaskTime;
import tianyi.task.ToDo;

/**
 * Converts stored task data into task objects.
 */
public class DataParser {
    private static final int FIELD_INDEX_TYPE = 0;
    private static final int FIELD_INDEX_STATUS = 1;
    private static final int FIELD_INDEX_DESCRIPTION = 2;
    private static final int FIELD_INDEX_DEADLINE = 3;
    private static final int FIELD_INDEX_EVENT_START = 3;
    private static final int FIELD_INDEX_EVENT_END = 4;

    private static final int FIELD_COUNT_MINIMUM = 3;
    private static final int FIELD_COUNT_TODO = 3;
    private static final int FIELD_COUNT_DEADLINE = 4;
    private static final int FIELD_COUNT_EVENT = 5;

    /**
     * Creates a parser for serialized task records.
     */
    public DataParser() {
    }

    /**
     * Converts one serialized task record into a task object.
     *
     * @param data Serialized task record.
     * @return Task represented by the record.
     * @throws StorageException If the record has an invalid type, status, or format.
     */
    public Task parse(String data)
            throws StorageException {
        String[] dataParts = data.split("\\s*\\|\\s*");

        if (dataParts.length < FIELD_COUNT_MINIMUM) {
            throw new StorageException("Invalid task data: " + data);
        }

        Task task = switch (dataParts[FIELD_INDEX_TYPE]) {
            case ToDo.TYPE_MARKER -> createTodo(dataParts, data);
            case Deadline.TYPE_MARKER -> createDeadline(dataParts, data);
            case Event.TYPE_MARKER -> createEvent(dataParts, data);
            default -> throw new StorageException("Unknown task type: " + data);
        };

        updateStatus(task, dataParts[FIELD_INDEX_STATUS]);
        return task;
    }

    /**
     * Parses a serialized todo record after validating its field count.
     */
    private Task createTodo(String[] dataParts, String data)
            throws StorageException {
        if (dataParts.length != FIELD_COUNT_TODO) {
            throw new StorageException("Invalid todo data: " + data);
        }

        return new ToDo(dataParts[FIELD_INDEX_DESCRIPTION]);
    }

    /**
     * Parses a serialized deadline record after validating its fields.
     */
    private Task createDeadline(String[] dataParts, String data)
            throws StorageException {
        if (dataParts.length != FIELD_COUNT_DEADLINE) {
            throw new StorageException("Invalid deadline data: " + data);
        }

        try {
            return new Deadline(
                    dataParts[FIELD_INDEX_DESCRIPTION],
                    new TaskTime(dataParts[FIELD_INDEX_DEADLINE])
            );
        } catch (DateTimeParseException e) {
            throw new StorageException(
                    "Invalid date and time in deadline data: " + dataParts[FIELD_INDEX_DEADLINE]);
        }
    }

    /**
     * Parses a serialized event record after validating its fields.
     */
    private Task createEvent(String[] dataParts, String data)
            throws StorageException {
        if (dataParts.length != FIELD_COUNT_EVENT) {
            throw new StorageException("Invalid event data: " + data);
        }

        try {
            return new Event(
                    dataParts[FIELD_INDEX_DESCRIPTION],
                    new TaskTime(dataParts[FIELD_INDEX_EVENT_START]),
                    new TaskTime(dataParts[FIELD_INDEX_EVENT_END])
            );
        } catch (DateTimeParseException e) {
            throw new StorageException("Invalid date and time in event data: " + data);
        }
    }

    /**
     * Restores a task's completion status from its serialized flag.
     */
    private void updateStatus(Task task, String status)
            throws StorageException {
        if (status.equals(Task.DATA_STATUS_DONE)) {
            task.markAsDone();
        } else if (!status.equals(Task.DATA_STATUS_NOT_DONE)) {
            throw new StorageException("Invalid task status: " + status);
        }
    }
}
