package tianyi.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import tianyi.TianyiException;
import tianyi.command.CommandTestFixture.RecordingStorage;
import tianyi.task.TaskList;
import tianyi.task.ToDo;

/**
 * Tests adding, saving, and reporting a task through {@link AddCommand}.
 */
public class AddCommandTest {
    @Test
    public void execute_validTask_addsSavesAndReturnsResponse()
            throws TianyiException {
        TaskList tasks = new TaskList();
        RecordingStorage storage = new RecordingStorage();
        AddCommand command = new AddCommand(new ToDo("read book"));

        String response = command.execute(tasks, storage);

        assertEquals(1, tasks.size());
        assertEquals("T | 0 | read book", tasks.getTasks().get(0).getData());
        assertNotNull(storage.savedTasks);
        assertEquals("T | 0 | read book", storage.savedTasks.get(0).getData());
        assertEquals("Got it. I've added this task:\n"
                + "  [T][ ] read book\n"
                + "Now you have 1 tasks in the list.", response);
    }
}
