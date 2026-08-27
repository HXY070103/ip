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
 * Tests marking, saving, and reporting a task through {@link MarkCommand}.
 */
public class MarkCommandTest {
    @Test
    public void execute_validIndex_marksSavesAndShowsResponse() throws TianyiException {
        TaskList tasks = new TaskList(List.of(new ToDo("first"), new ToDo("second")));
        RecordingUi ui = new RecordingUi();
        RecordingStorage storage = new RecordingStorage();
        MarkCommand command = new MarkCommand(1);

        command.execute(tasks, ui, storage);

        assertEquals("T | 0 | first", tasks.getTasks().get(0).getData());
        assertEquals("T | 1 | second", tasks.getTasks().get(1).getData());
        assertNotNull(storage.savedTasks);
        assertEquals("T | 1 | second", storage.savedTasks.get(1).getData());
        assertEquals("Nice! I've marked this task as done:\n"
                + "  [T][X] second", ui.response);
    }
}
