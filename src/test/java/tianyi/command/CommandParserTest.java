package tianyi.command;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import tianyi.TianyiException;
import tianyi.storage.Storage;
import tianyi.task.Deadline;
import tianyi.task.Event;
import tianyi.task.Task;
import tianyi.task.TaskList;
import tianyi.task.TaskTime;
import tianyi.task.ToDo;
import tianyi.ui.Ui;

/**
 * Tests parsing and validation for all supported command forms.
 */
public class CommandParserTest {
    private final CommandParser parser = new CommandParser();
    private final TestUi ui = new TestUi();
    private final Storage storage = new NoOpStorage();

    @Test
    public void parse_todoCommand_addsTodoWithOriginalDescription() throws TianyiException {
        TaskList tasks = new TaskList();

        parser.parse("ToDo Buy Milk", tasks).execute(tasks, ui, storage);

        Task task = tasks.getTasks().get(0);
        assertInstanceOf(ToDo.class, task);
        assertEquals("T | 0 | Buy Milk", task.getData());
    }

    @Test
    public void parse_deadlineCommand_addsDeadlineWithDateAndTime() throws TianyiException {
        TaskList tasks = new TaskList();

        parser.parse("deadline submit report /by 2-12-2019 18:00", tasks)
                .execute(tasks, ui, storage);

        Task task = tasks.getTasks().get(0);
        assertInstanceOf(Deadline.class, task);
        assertEquals("D | 0 | submit report | 2-12-2019 18:00", task.getData());
    }

    @Test
    public void parse_eventCommand_addsEventWithDateRange() throws TianyiException {
        TaskList tasks = new TaskList();

        parser.parse("event workshop /from 2-12-2019 /to 3-12-2019 16:00", tasks)
                .execute(tasks, ui, storage);

        Task task = tasks.getTasks().get(0);
        assertInstanceOf(Event.class, task);
        assertEquals("E | 0 | workshop | 2-12-2019 | 3-12-2019 16:00", task.getData());
    }

    @Test
    public void parse_markCommand_marksRequestedTask() throws TianyiException {
        TaskList tasks = createTwoTodoTasks();

        parser.parse("mark 2", tasks).execute(tasks, ui, storage);

        assertEquals("T | 0 | first", tasks.getTasks().get(0).getData());
        assertEquals("T | 1 | second", tasks.getTasks().get(1).getData());
    }

    @Test
    public void parse_unmarkCommand_unmarksRequestedTask() throws TianyiException {
        TaskList tasks = createTwoTodoTasks();
        tasks.markTask(0);

        parser.parse("unmark 1", tasks).execute(tasks, ui, storage);

        assertEquals("T | 0 | first", tasks.getTasks().get(0).getData());
    }

    @Test
    public void parse_deleteCommand_deletesRequestedTask() throws TianyiException {
        TaskList tasks = createTwoTodoTasks();

        parser.parse("delete 2", tasks).execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        assertEquals("T | 0 | first", tasks.getTasks().get(0).getData());
    }

    @Test
    public void parse_listWithoutDate_listsAllTasks() throws TianyiException {
        TaskList tasks = createTwoTodoTasks();

        parser.parse("list", tasks).execute(tasks, ui, storage);

        assertEquals("Here are the tasks in your list:\n"
                + "1.[T][ ] first\n"
                + "2.[T][ ] second", ui.response);
    }

    @Test
    public void parse_listWithDate_listsTasksOccurringOnDate() throws TianyiException {
        TaskList tasks = new TaskList(List.of(
                new ToDo("buy milk"),
                new Deadline("submit report", new TaskTime("3-12-2019 18:00")),
                new Event("workshop", new TaskTime("2-12-2019"),
                        new TaskTime("3-12-2019 16:00"))));

        parser.parse("list 2-12-2019", tasks).execute(tasks, ui, storage);

        assertEquals("Here are deadlines/events occurring on 2-12-2019:\n"
                + "1.[D][ ] submit report (by: Tue, Dec 03 2019, 6:00 PM)\n"
                + "2.[E][ ] workshop (from: Mon, Dec 02 2019 "
                + "to: Tue, Dec 03 2019, 4:00 PM)", ui.response);
    }

    @Test
    public void parse_byeCommand_returnsExitCommand() throws TianyiException {
        Command command = parser.parse("bye", new TaskList());

        assertInstanceOf(ExitCommand.class, command);
        assertTrue(command.isExit());
    }

    @Test
    public void parse_nonExitCommand_isExitReturnsFalse() throws TianyiException {
        Command command = parser.parse("list", new TaskList());

        assertFalse(command.isExit());
    }

    @Test
    public void parse_emptyAddCommandArguments_exceptionThrown() {
        assertAll(
                () -> assertParseFails("todo", new TaskList(),
                        "The argument of todo command cannot be empty.\n"
                                + "Try: todo borrow book"),
                () -> assertParseFails("deadline", new TaskList(),
                        "The argument of deadline command cannot be empty.\n"
                                + "Try: deadline return book /by 2-12-2019 18:00"),
                () -> assertParseFails("event", new TaskList(),
                        "The argument of event command cannot be empty.\n"
                                + "Try: event meeting /from 2-12-2019 14:00 "
                                + "/to 2-12-2019 16:00")
        );
    }

    @Test
    public void parse_malformedDeadlineArguments_exceptionThrown() {
        String example = "Try: deadline return book /by 2-12-2019 18:00";

        assertAll(
                () -> assertParseFails("deadline return book", new TaskList(),
                        "Deadline command must contain /by.\n" + example),
                () -> assertParseFails("deadline /by 2-12-2019", new TaskList(),
                        "The description of deadline command cannot be empty.\n" + example),
                () -> assertParseFails("deadline return book /by", new TaskList(),
                        "The by date of deadline command cannot be empty.\n" + example),
                () -> assertParseFails("deadline return book /by 31-2-2019", new TaskList(),
                        "Invalid deadline date or time. "
                                + "Please use d-M-yyyy with optional HH:mm.\n" + example)
        );
    }

    @Test
    public void parse_malformedEventArguments_exceptionThrown() {
        String example = "Try: event meeting /from 2-12-2019 14:00 /to 2-12-2019 16:00";

        assertAll(
                () -> assertParseFails("event meeting", new TaskList(),
                        "Event command must contain /from.\n" + example),
                () -> assertParseFails("event /from 2-12-2019 /to 3-12-2019",
                        new TaskList(),
                        "The description of event command cannot be empty.\n" + example),
                () -> assertParseFails("event meeting /from", new TaskList(),
                        "Event command must contain /to.\n" + example),
                () -> assertParseFails("event meeting /from /to 3-12-2019",
                        new TaskList(),
                        "The from date of event command cannot be empty.\n" + example),
                () -> assertParseFails("event meeting /from 2-12-2019 /to", new TaskList(),
                        "The to date of event command cannot be empty.\n" + example),
                () -> assertParseFails("event meeting /from invalid /to 3-12-2019",
                        new TaskList(),
                        "Invalid event date or time. "
                                + "Please use d-M-yyyy with optional HH:mm.\n" + example)
        );
    }

    @Test
    public void parse_taskNumberMissing_exceptionThrown() {
        assertParseFails("mark", createTwoTodoTasks(),
                "Please specify a task number.\nTry: mark 1");
    }

    @Test
    public void parse_taskNumberWithEmptyList_exceptionThrown() {
        assertParseFails("delete 1", new TaskList(),
                "There is no task in your list.\n"
                        + "Please add a task.\n"
                        + "Try: todo borrow book");
    }

    @Test
    public void parse_invalidTaskNumbers_exceptionThrown() {
        TaskList tasks = createTwoTodoTasks();

        assertAll(
                () -> assertParseFails("mark abc", tasks,
                        "abc is not a valid task number.\nTry: mark 1"),
                () -> assertParseFails("mark 0", tasks,
                        "Task number 0 does not exist.\n"
                                + "Please enter a number from 1 to 2.Try: mark 1"),
                () -> assertParseFails("mark 3", tasks,
                        "Task number 3 does not exist.\n"
                                + "Please enter a number from 1 to 2.Try: mark 1")
        );
    }

    @Test
    public void parse_listWithInvalidDate_exceptionThrown() {
        assertAll(
                () -> assertParseFails("list 31-2-2019", new TaskList(),
                        "Invalid list date.\nPlease use d-M-yyyy.\nTry: list 2-12-2019"),
                () -> assertParseFails("list 2-12-2019 18:00", new TaskList(),
                        "Invalid list date.\nPlease use d-M-yyyy.\nTry: list 2-12-2019")
        );
    }

    @Test
    public void parse_byeWithArgument_exceptionThrown() {
        assertParseFails("bye now", new TaskList(),
                "Bye command does not accept any arguments.\nTry: bye");
    }

    @Test
    public void parse_unknownCommand_exceptionThrown() {
        assertParseFails("abracadabra", new TaskList(),
                "I'm sorry, but I don't know what that means.");
    }

    private TaskList createTwoTodoTasks() {
        return new TaskList(List.of(new ToDo("first"), new ToDo("second")));
    }

    private void assertParseFails(String input, TaskList tasks, String expectedMessage) {
        TianyiException exception = assertThrows(
                TianyiException.class, () -> parser.parse(input, tasks));

        assertEquals(expectedMessage, exception.getMessage());
    }

    /**
     * Captures command responses without writing to standard output.
     */
    private static class TestUi extends Ui {
        private String response;

        @Override
        public void showResponse(String response) {
            this.response = response;
        }
    }

    /**
     * Avoids file-system writes while exercising parsed commands.
     */
    private static class NoOpStorage extends Storage {
        NoOpStorage() {
            super("unused");
        }

        @Override
        public void save(List<Task> tasks) {
            // Saving is outside the scope of parser tests.
        }
    }
}
