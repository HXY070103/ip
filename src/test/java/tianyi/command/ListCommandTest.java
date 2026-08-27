package tianyi.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import tianyi.command.CommandTestFixture.RecordingStorage;
import tianyi.command.CommandTestFixture.RecordingUi;
import tianyi.task.Deadline;
import tianyi.task.TaskList;
import tianyi.task.TaskTime;
import tianyi.task.ToDo;

/**
 * Tests listing all tasks or tasks relevant to a date through {@link ListCommand}.
 */
public class ListCommandTest {
    @Test
    public void execute_withoutDate_showsAllTasksWithoutSaving() {
        TaskList tasks = new TaskList(List.of(new ToDo("read book")));
        RecordingUi ui = new RecordingUi();
        RecordingStorage storage = new RecordingStorage();
        ListCommand command = new ListCommand(null);

        command.execute(tasks, ui, storage);

        assertEquals("Here are the tasks in your list:\n"
                + "1.[T][ ] read book", ui.response);
        assertNull(storage.savedTasks);
    }

    @Test
    public void execute_withDate_showsMatchingTasksWithoutSaving() {
        TaskList tasks = new TaskList(List.of(
                new ToDo("read book"),
                new Deadline("submit report", new TaskTime("3-12-2019"))));
        RecordingUi ui = new RecordingUi();
        RecordingStorage storage = new RecordingStorage();
        ListCommand command = new ListCommand(new TaskTime("2-12-2019"));

        command.execute(tasks, ui, storage);

        assertEquals("Here are deadlines/events occurring on 2-12-2019:\n"
                + "1.[D][ ] submit report (by: Tue, Dec 03 2019)", ui.response);
        assertNull(storage.savedTasks);
    }
}
