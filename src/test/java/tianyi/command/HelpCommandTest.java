package tianyi.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import tianyi.Response;
import tianyi.command.CommandTestFixture.RecordingStorage;
import tianyi.task.TaskList;

/**
 * Tests help display behavior of {@link HelpCommand}.
 */
public class HelpCommandTest {
    @Test
    public void execute_always_returnsCommandDescriptionsWithoutSaving() {
        RecordingStorage storage = new RecordingStorage();

        Response response = new HelpCommand().execute(new TaskList(), storage);

        assertEquals("Here is the list of commands:", response.getHeader());
        assertEquals(CommandType.getCommands(), response.getMessage());
        assertNull(storage.savedTasks);
    }
}
