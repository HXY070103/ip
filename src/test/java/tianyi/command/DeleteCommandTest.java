package tianyi.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import tianyi.TianyiException;
import tianyi.command.CommandTestFixture.RecordingStorage;
import tianyi.command.CommandTestFixture.RecordingUi;
import tianyi.task.TaskList;
import tianyi.task.ToDo;

/**
 * Tests deleting, saving, and reporting a task through {@link DeleteCommand}.
 */
public class DeleteCommandTest {
    @Test
    public void execute_validIndex_deletesSavesAndShowsResponse()
            throws TianyiException {
        TaskList tasks = new TaskList(List.of(new ToDo("first"), new ToDo("second")));
        RecordingUi ui = new RecordingUi();
        RecordingStorage storage = new RecordingStorage();
        DeleteCommand command = new DeleteCommand(0);

        command.execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        assertEquals("T | 0 | second", tasks.getTasks().get(0).getData());
        assertNotNull(storage.savedTasks);
        assertEquals(1, storage.savedTasks.size());
        assertEquals("T | 0 | second", storage.savedTasks.get(0).getData());
        assertEquals("Noted. I've removed this task:\n"
                + "  [T][ ] first\n"
                + "Now you have 1 tasks in the list.", ui.response);
    }
}
