import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Represents a required task date with an optional time.
 */
public class TaskTime {
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("d-M-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm")
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEE, MMM dd uuuu", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_TIME_FORMAT =
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    private final LocalDate date;
    private final LocalTime time;
    private final boolean hasTime;

    public TaskTime(String input) {
        String[] dateTimeParts = input.trim().split("\\s+", 2);
        date = LocalDate.parse(dateTimeParts[0], DATE_FORMAT);

        if (dateTimeParts.length == 1) {
            time = null;
            hasTime = false;
        } else {
            time = LocalTime.parse(dateTimeParts[1], TIME_FORMAT);
            hasTime = true;
        }
    }

    public boolean hasTime() {
        return hasTime;
    }

    public boolean isBefore(TaskTime taskTime) {
        return date.isBefore(taskTime.date);
    }

    public boolean isAfter(TaskTime taskTime) {
        return date.isAfter(taskTime.date);
    }

    public String getData() {
        return hasTime
                ? date.format(DATE_FORMAT) + " " + time.format(TIME_FORMAT)
                : date.format(DATE_FORMAT);
    }

    @Override
    public String toString() {
        return hasTime
                ? date.format(DISPLAY_DATE_FORMAT) + ", " + time.format(DISPLAY_TIME_FORMAT)
                : date.format(DISPLAY_DATE_FORMAT);
    }
}
