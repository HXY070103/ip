package tianyi.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores tasks and provides operations for managing and displaying them.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing a defensive copy of the supplied tasks.
     *
     * @param tasks Initial tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Reports whether this list contains no tasks.
     *
     * @return {@code true} if the list is empty, otherwise {@code false}.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return Task count.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an unmodifiable snapshot of the current tasks.
     *
     * @return Copy of the current task sequence.
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Adds a task and formats a confirmation message.
     *
     * @param task Task to add.
     * @return Confirmation containing the task and updated count.
     */
    public String addTask(Task task) {
        tasks.add(task);

        return "Got it. I've added this task:\n"
                + "  " + task + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Deletes a task and formats a confirmation message.
     *
     * @param index Zero-based index of the task to delete.
     * @return Confirmation containing the removed task and updated count.
     */
    public String deleteTask(int index) {
        Task task = tasks.get(index);
        tasks.remove(index);

        return "Noted. I've removed this task:\n"
                + "  " + task + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Marks a task as completed and formats a confirmation message.
     *
     * @param index Zero-based index of the task to mark.
     * @return Confirmation containing the updated task.
     */
    public String markTask(int index) {
        Task task = tasks.get(index);
        task.markAsDone();

        return "Nice! I've marked this task as done:\n"
                + "  " + task;
    }

    /**
     * Marks a task as incomplete and formats a confirmation message.
     *
     * @param index Zero-based index of the task to unmark.
     * @return Confirmation containing the updated task.
     */
    public String unmarkTask(int index) {
        Task task = tasks.get(index);
        task.unmarkAsDone();

        return "OK, I've marked this task as not done yet:\n"
                + "  " + task;
    }

    /**
     * Formats every task in this list for display.
     *
     * @return Numbered task list, or a message indicating that no tasks exist.
     */
    public String listTasks() {
        return formatTasks(tasks, "Here are the tasks in your list:");
    }

    /**
     * Formats dated tasks that occur on the specified date.
     *
     * @param time Date used to select deadlines and events.
     * @return Numbered matching tasks, or a message indicating that none match.
     */
    public String listTasks(TaskTime time) {
        List<Task> occurringTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.isOccurringOn(time)) {
                occurringTasks.add(task);
            }
        }

        return formatTasks(
                occurringTasks,
                "Here are deadlines/events occurring on " + time.getData() + ":"
        );
    }

    /**
     * Formats tasks whose descriptions contain the specified keyword.
     *
     * @param keyword Keyword used to select tasks.
     * @return Numbered matching tasks, or a message indicating that none match.
     */
    public String listTasks(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.matchesKeyword(keyword)) {
                matchingTasks.add(task);
            }
        }

        return formatTasks(
                matchingTasks,
                "Here are the matching tasks in your list:"
        );
    }

    /**
     * Builds a numbered display string for a collection of tasks.
     */
    private String formatTasks(List<Task> tasksToDisplay, String heading) {
        if (tasksToDisplay.isEmpty()) {
            return "No tasks found.";
        }

        StringBuilder content = new StringBuilder(heading).append("\n");

        for (int i = 0; i < tasksToDisplay.size(); i += 1) {
            content.append(i + 1).append(".").append(tasksToDisplay.get(i));

            if (i < tasksToDisplay.size() - 1) {
                content.append("\n");
            }
        }

        return content.toString();
    }
}
