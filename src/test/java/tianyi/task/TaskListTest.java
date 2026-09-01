package tianyi.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests task collection updates, response messages, and date filtering.
 */
public class TaskListTest {
    @Test
    public void constructor_sourceListChanged_taskListRemainsUnchanged() {
        List<Task> source = new ArrayList<>(List.of(new ToDo("first")));
        TaskList tasks = new TaskList(source);

        source.add(new ToDo("second"));

        assertEquals(1, tasks.size());
        assertEquals("T | 0 | first", tasks.getTasks().get(0).getData());
    }

    @Test
    public void getTasks_returnedListCannotModifyTaskList() {
        TaskList tasks = new TaskList(List.of(new ToDo("first")));

        assertThrows(UnsupportedOperationException.class, () -> tasks.getTasks().add(new ToDo("second")));
        assertEquals(1, tasks.size());
    }

    @Test
    public void addTask_validTask_addsTaskAndReturnsUpdatedCount() {
        TaskList tasks = new TaskList();

        String response = tasks.addTask(new ToDo("read book"));

        assertEquals("Got it. I've added this task:\n"
                + "  [T][ ] read book\n"
                + "Now you have 1 tasks in the list.", response);
        assertEquals(1, tasks.size());
    }

    @Test
    public void deleteTask_middleTask_removesTaskAndReturnsUpdatedCount() {
        TaskList tasks = new TaskList(List.of(
                new ToDo("first"), new ToDo("second"), new ToDo("third")));

        String response = tasks.deleteTask(1);

        assertEquals("Noted. I've removed this task:\n"
                + "  [T][ ] second\n"
                + "Now you have 2 tasks in the list.", response);
        assertEquals("T | 0 | first", tasks.getTasks().get(0).getData());
        assertEquals("T | 0 | third", tasks.getTasks().get(1).getData());
    }

    @Test
    public void markTask_validIndex_marksTaskAndReturnsResponse() {
        TaskList tasks = new TaskList(List.of(new ToDo("read book")));

        String response = tasks.markTask(0);

        assertEquals("Nice! I've marked this task as done:\n"
                + "  [T][X] read book", response);
        assertEquals("T | 1 | read book", tasks.getTasks().get(0).getData());
    }

    @Test
    public void unmarkTask_markedTask_unmarksTaskAndReturnsResponse() {
        ToDo task = new ToDo("read book");
        task.markAsDone();
        TaskList tasks = new TaskList(List.of(task));

        String response = tasks.unmarkTask(0);

        assertEquals("OK, I've marked this task as not done yet:\n"
                + "  [T][ ] read book", response);
        assertEquals("T | 0 | read book", tasks.getTasks().get(0).getData());
    }

    @Test
    public void listTasks_emptyList_noTasksFound() {
        assertEquals("No tasks found.", new TaskList().listTasks());
    }

    @Test
    public void listTasks_multipleTasks_listsInOriginalOrderWithNumbers() {
        ToDo completedTask = new ToDo("first");
        completedTask.markAsDone();
        TaskList tasks = new TaskList(List.of(completedTask, new ToDo("second")));

        assertEquals("Here are the tasks in your list:\n"
                + "1.[T][X] first\n"
                + "2.[T][ ] second", tasks.listTasks());
    }

    @Test
    public void listTasks_date_filtersTodosExpiredDeadlinesAndInactiveEvents() {
        TaskList tasks = new TaskList(List.of(
                new ToDo("todo"),
                new Deadline("expired", new TaskTime("1-12-2019")),
                new Deadline("active deadline", new TaskTime("3-12-2019")),
                new Event("past event", new TaskTime("30-11-2019"),
                        new TaskTime("1-12-2019")),
                new Event("active event", new TaskTime("2-12-2019"),
                        new TaskTime("3-12-2019")),
                new Event("future event", new TaskTime("3-12-2019"),
                        new TaskTime("4-12-2019"))));

        String response = tasks.listTasks(new TaskTime("2-12-2019"));

        assertEquals("Here are deadlines/events occurring on 2-12-2019:\n"
                + "1.[D][ ] active deadline (by: Tue, Dec 03 2019)\n"
                + "2.[E][ ] active event (from: Mon, Dec 02 2019 "
                + "to: Tue, Dec 03 2019)", response);
    }

    @Test
    public void listTasks_dateWithNoMatches_noTasksFound() {
        TaskList tasks = new TaskList(List.of(
                new ToDo("todo"),
                new Event("past event", new TaskTime("1-12-2019"),
                        new TaskTime("2-12-2019"))));

        assertEquals("No tasks found.", tasks.listTasks(new TaskTime("3-12-2019")));
    }

    @Test
    public void listTasks_keyword_listsMatchingTasksInOriginalOrder() {
        TaskList tasks = new TaskList(List.of(
                new ToDo("read book"),
                new ToDo("buy milk"),
                new ToDo("return book")));

        String response = tasks.listTasks("book");

        assertEquals("Here are the matching tasks in your list:\n"
                + "1.[T][ ] read book\n"
                + "2.[T][ ] return book", response);
    }

    @Test
    public void listTasks_keywordWithNoMatches_noTasksFound() {
        TaskList tasks = new TaskList(List.of(new ToDo("read book")));

        assertEquals("No tasks found.", tasks.listTasks("milk"));
    }
}
