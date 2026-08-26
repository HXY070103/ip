import java.util.ArrayList;
import java.util.List;

/**
 * Stores tasks and provides operations for managing and displaying them.
 */
public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public int size() {
        return tasks.size();
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    public String addTask(Task task) {
        tasks.add(task);

        return "Got it. I've added this task:\n"
                + "  " + task + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.";
    }

    public String deleteTask(int index) {
        Task task = tasks.get(index);
        tasks.remove(index);

        return "Noted. I've removed this task:\n"
                + "  " + task + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.";
    }

    public String markTask(int index) {
        Task task = tasks.get(index);
        task.markAsDone();

        return "Nice! I've marked this task as done:\n"
                + "  " + task;
    }

    public String unmarkTask(int index) {
        Task task = tasks.get(index);
        task.unmarkAsDone();

        return "OK, I've marked this task as not done yet:\n"
                + "  " + task;
    }

    public String listTasks() {
        return formatTasks(tasks, "Here are the tasks in your list:");
    }

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
