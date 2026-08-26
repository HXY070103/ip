/**
 * Represents a task occurring between dates with optional times.
 */
public class Event extends Task {
    private final TaskTime startTime;
    private final TaskTime endTime;

    public Event(String description, TaskTime startTime, TaskTime endTime) {
        super(description);

        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String getData() {
        return "E | " + super.getData() + " | " + startTime.getData() + " | " + endTime.getData();
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + startTime + " to: " + endTime + ")";
    }
}
