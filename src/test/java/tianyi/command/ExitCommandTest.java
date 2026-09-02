package tianyi.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import tianyi.command.CommandTestFixture.RecordingStorage;
import tianyi.task.TaskList;

/**
 * Tests exit signaling and goodbye behavior of {@link ExitCommand}.
 */
public class ExitCommandTest {
    @Test
    public void execute_always_returnsGoodbyeWithoutSaving() {
        TaskList tasks = new TaskList();
        RecordingStorage storage = new RecordingStorage();
        ExitCommand command = new ExitCommand();

        String response = command.execute(tasks, storage);

        assertEquals("Bye. Hope to see you again soon!", response);
        assertNull(storage.savedTasks);
        assertTrue(command.isExit());
    }
}
