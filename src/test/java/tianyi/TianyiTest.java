package tianyi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests application startup, command-loop control, error recovery, and persistence.
 */
public class TianyiTest {
    private static final String LINE =
            "____________________________________________________________";
    private static final String NEWLINE = System.lineSeparator();
    private static final String WELCOME_OUTPUT = LINE + NEWLINE
            + " _____ _                   _\n"
            + "|_   _(_) __ _ _ __  _   _(_)\n"
            + "  | | | |/ _` | '_ \\| | | | |\n"
            + "  | | | | (_| | | | | |_| | |\n"
            + "  |_| |_|\\__,_|_| |_|\\__, |_|\n"
            + "                     |___/\n"
            + "Hello! I'm Tianyi.\n"
            + "What can I do for you?" + NEWLINE
            + LINE + NEWLINE;
    private static final String GOODBYE_OUTPUT = LINE + NEWLINE
            + "Bye. Hope to see you again soon!" + NEWLINE
            + LINE + NEWLINE;

    @TempDir
    private Path tempDir;

    private InputStream originalInput;
    private PrintStream originalOutput;
    private ByteArrayOutputStream output;

    @BeforeEach
    public void setUpStreams() {
        originalInput = System.in;
        originalOutput = System.out;
        output = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(new byte[0]));
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    public void restoreStreams() {
        System.setIn(originalInput);
        System.setOut(originalOutput);
    }

    @Test
    public void run_endOfInput_showsWelcomeAndStops() {
        Tianyi tianyi = createTianyi("", tempDir.resolve("missing.txt"));

        tianyi.run();

        assertEquals(WELCOME_OUTPUT, getOutput());
    }

    @Test
    public void run_blankCommandsThenBye_ignoresBlankCommandsAndExits() {
        Tianyi tianyi = createTianyi("\n   \nbye\n", tempDir.resolve("tasks.txt"));

        tianyi.run();

        assertEquals(WELCOME_OUTPUT + GOODBYE_OUTPUT, getOutput());
    }

    @Test
    public void run_existingTasks_listShowsLoadedTasks()
            throws IOException {
        Path dataFile = tempDir.resolve("tasks.txt");
        Files.writeString(dataFile, "T | 1 | read book" + NEWLINE);
        Tianyi tianyi = createTianyi("list\nbye\n", dataFile);

        tianyi.run();

        String listOutput = LINE + NEWLINE
                + "Here are the tasks in your list:\n"
                + "1.[T][X] read book" + NEWLINE
                + LINE + NEWLINE;
        assertEquals(WELCOME_OUTPUT + listOutput + GOODBYE_OUTPUT, getOutput());
    }

    @Test
    public void constructor_invalidStoredData_showsErrorAndStartsWithEmptyList()
            throws IOException {
        Path dataFile = tempDir.resolve("invalid.txt");
        Files.writeString(dataFile, "X | 0 | invalid" + NEWLINE);
        Tianyi tianyi = createTianyi("list\nbye\n", dataFile);

        tianyi.run();

        String loadErrorOutput = LINE + NEWLINE
                + "Oops! Unknown task type: X | 0 | invalid" + NEWLINE
                + LINE + NEWLINE;
        String emptyListOutput = LINE + NEWLINE
                + "No tasks found." + NEWLINE
                + LINE + NEWLINE;
        assertEquals(loadErrorOutput + WELCOME_OUTPUT + emptyListOutput + GOODBYE_OUTPUT,
                getOutput());
    }

    @Test
    public void run_unknownCommand_showsErrorAndContinuesToBye() {
        Tianyi tianyi = createTianyi("abracadabra\nbye\n", tempDir.resolve("tasks.txt"));

        tianyi.run();

        String commandErrorOutput = LINE + NEWLINE
                + "Oops! I'm sorry, but I don't know what that means." + NEWLINE
                + LINE + NEWLINE;
        assertEquals(WELCOME_OUTPUT + commandErrorOutput + GOODBYE_OUTPUT, getOutput());
    }

    @Test
    public void run_addCommand_savesTaskToConfiguredFile()
            throws IOException {
        Path dataFile = tempDir.resolve("nested/tasks.txt");
        Tianyi tianyi = createTianyi("todo read book\nbye\n", dataFile);

        tianyi.run();

        assertTrue(Files.isRegularFile(dataFile));
        assertEquals("T | 0 | read book" + NEWLINE, Files.readString(dataFile));
    }

    private Tianyi createTianyi(String input, Path dataFile) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        return new Tianyi(dataFile.toString());
    }

    private String getOutput() {
        return output.toString(StandardCharsets.UTF_8);
    }
}
