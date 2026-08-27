package tianyi.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import tianyi.TianyiException;
import tianyi.command.CommandTestFixture.RecordingStorage;
import tianyi.command.CommandTestFixture.RecordingUi;
import tianyi.task.TaskList;
import tianyi.task.ToDo;

/**
 * Tests filtering and reporting tasks through {@link FindCommand}.
 */
public class FindCommandTest {
    @Test
    public void execute_matchingTasks_showsMatchesWithoutSaving()
            throws TianyiException {
        TaskList tasks = new TaskList(List.of(
                new ToDo("read book"),
                new ToDo("buy milk"),
                new ToDo("return book")));
        RecordingUi ui = new RecordingUi();
        RecordingStorage storage = new RecordingStorage();
        FindCommand command = new FindCommand("book");

        command.execute(tasks, ui, storage);

        assertEquals("Here are the matching tasks in your list:\n"
                + "1.[T][ ] read book\n"
                + "2.[T][ ] return book", ui.response);
        assertNull(storage.savedTasks);
    }

    @Test
    public void execute_noMatchingTasks_showsNoTasksFoundWithoutSaving()
            throws TianyiException {
        TaskList tasks = new TaskList(List.of(new ToDo("read book")));
        RecordingUi ui = new RecordingUi();
        RecordingStorage storage = new RecordingStorage();
        FindCommand command = new FindCommand("milk");

        command.execute(tasks, ui, storage);

        assertEquals("No tasks found.", ui.response);
        assertNull(storage.savedTasks);
    }
}
