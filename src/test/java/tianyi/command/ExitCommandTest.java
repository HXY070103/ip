package tianyi.command;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import tianyi.command.CommandTestFixture.RecordingStorage;
import tianyi.command.CommandTestFixture.RecordingUi;
import tianyi.task.TaskList;

/**
 * Tests exit signaling and goodbye behavior of {@link ExitCommand}.
 */
public class ExitCommandTest {
    @Test
    public void execute_always_showsGoodbyeWithoutSaving() {
        TaskList tasks = new TaskList();
        RecordingUi ui = new RecordingUi();
        RecordingStorage storage = new RecordingStorage();
        ExitCommand command = new ExitCommand();

        command.execute(tasks, ui, storage);

        assertTrue(ui.goodbyeShown);
        assertNull(ui.response);
        assertNull(storage.savedTasks);
        assertTrue(command.isExit());
    }
}
