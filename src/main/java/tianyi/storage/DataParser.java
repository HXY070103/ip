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
    /**
     * Creates a parser for serialized task records.
     */
    public DataParser() {
    }

    /**
     * Converts one serialized task record into a task object.
     *
     * @param data serialized task record
     * @return task represented by the record
     * @throws StorageException if the record has an invalid type, status, or format
     */
    public Task parse(String data) throws StorageException {
        String[] dataParts = data.split("\\s*\\|\\s*");

        if (dataParts.length < 3) {
            throw new StorageException("Invalid task data: " + data);
        }

        Task task;

        switch (dataParts[0]) {
            case "T":
                task = createTodo(dataParts, data);
                break;
            case "D":
                task = createDeadline(dataParts, data);
                break;
            case "E":
                task = createEvent(dataParts, data);
                break;
            default:
                throw new StorageException("Unknown task type: " + data);
        }

        updateStatus(task, dataParts[1]);
        return task;
    }

    /**
     * Parses a serialized todo record after validating its field count.
     */
    private Task createTodo(String[] dataParts, String data) throws StorageException {
        if (dataParts.length != 3) {
            throw new StorageException("Invalid todo data: " + data);
        }

        return new ToDo(dataParts[2]);
    }

    /**
     * Parses a serialized deadline record after validating its fields.
     */
    private Task createDeadline(String[] dataParts, String data) throws StorageException {
        if (dataParts.length != 4) {
            throw new StorageException("Invalid deadline data: " + data);
        }

        try {
            return new Deadline(dataParts[2], new TaskTime(dataParts[3]));
        } catch (DateTimeParseException e) {
            throw new StorageException("Invalid date and time in deadline data: " + dataParts[3]);
        }
    }

    /**
     * Parses a serialized event record after validating its fields.
     */
    private Task createEvent(String[] dataParts, String data) throws StorageException {
        if (dataParts.length != 5) {
            throw new StorageException("Invalid event data: " + data);
        }

        try {
            return new Event(dataParts[2], new TaskTime(dataParts[3]), new TaskTime(dataParts[4]));
        } catch (DateTimeParseException e) {
            throw new StorageException("Invalid date and time in event data: " + data);
        }
    }

    /**
     * Restores a task's completion status from its serialized flag.
     */
    private void updateStatus(Task task, String status) throws StorageException {
        if (status.equals("1")) {
            task.markAsDone();
        } else if (!status.equals("0")) {
            throw new StorageException("Invalid task status: " + status);
        }
    }
}
