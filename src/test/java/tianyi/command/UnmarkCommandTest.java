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
 * Tests unmarking, saving, and reporting a task through {@link UnmarkCommand}.
 */
public class UnmarkCommandTest {
    @Test
    public void execute_validIndex_unmarksSavesAndShowsResponse()
            throws TianyiException {
        ToDo task = new ToDo("read book");
        task.markAsDone();
        TaskList tasks = new TaskList(List.of(task));
        RecordingUi ui = new RecordingUi();
        RecordingStorage storage = new RecordingStorage();
        UnmarkCommand command = new UnmarkCommand(0);

        command.execute(tasks, ui, storage);

        assertEquals("T | 0 | read book", tasks.getTasks().get(0).getData());
        assertNotNull(storage.savedTasks);
        assertEquals("T | 0 | read book", storage.savedTasks.get(0).getData());
        assertEquals("OK, I've marked this task as not done yet:\n"
                + "  [T][ ] read book", ui.response);
    }
}
