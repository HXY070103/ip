/**
 * Represents a task that must be completed by a specific date and optional time.
 */
public class Deadline extends Task {
    private final TaskTime deadline;

    public Deadline(String description, TaskTime deadline) {
        super(description);

        this.deadline = deadline;
    }

    @Override
    public String getData() {
        return "D | " + super.getData() + " | " + deadline.getData();
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + deadline + ")";
    }
}
