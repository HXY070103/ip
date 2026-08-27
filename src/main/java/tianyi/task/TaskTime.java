package tianyi.task;

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
            DateTimeFormatter.ofPattern("d-M-uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEE, MMM dd uuuu", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_TIME_FORMAT =
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    private final LocalDate date;
    private final LocalTime time;
    private final boolean hasTime;

    /**
     * Parses a task date with an optional time.
     *
     * @param input date in {@code d-M-yyyy} format, optionally followed by {@code HH:mm}
     * @throws java.time.format.DateTimeParseException if the date or time is invalid
     */
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

    /**
     * Reports whether this value includes a time of day.
     *
     * @return {@code true} if a time was supplied, otherwise {@code false}
     */
    public boolean hasTime() {
        return hasTime;
    }

    /**
     * Compares this value's date with another value's date.
     *
     * @param time value whose date is compared with this date
     * @return {@code true} if this date is earlier than the other date
     */
    public boolean isBefore(TaskTime time) {
        return date.isBefore(time.date);
    }

    /**
     * Compares this value's date with another value's date.
     *
     * @param time value whose date is compared with this date
     * @return {@code true} if this date is later than the other date
     */
    public boolean isAfter(TaskTime time) {
        return date.isAfter(time.date);
    }

    /**
     * Formats this value in the machine-readable storage format.
     *
     * @return date and optional time in their accepted input formats
     */
    public String getData() {
        return hasTime
                ? date.format(DATE_FORMAT) + " " + time.format(TIME_FORMAT)
                : date.format(DATE_FORMAT);
    }

    /**
     * Formats this value as a human-readable English date and optional time.
     *
     * @return display representation of this date and optional time
     */
    @Override
    public String toString() {
        return hasTime
                ? date.format(DISPLAY_DATE_FORMAT) + ", " + time.format(DISPLAY_TIME_FORMAT)
                : date.format(DISPLAY_DATE_FORMAT);
    }
}
