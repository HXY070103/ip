package tianyi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    private static final String FILE_NEWLINE = System.lineSeparator();
    private static final String WELCOME_MESSAGE =
            " _____ _                   _\n"
            + "|_   _(_) __ _ _ __  _   _(_)\n"
            + "  | | | |/ _` | '_ \\| | | | |\n"
            + "  | | | | (_| | | | | |_| | |\n"
            + "  |_| |_|\\__,_|_| |_|\\__, |_|\n"
            + "                     |___/\n"
            + "Hello! I'm Tianyi.\n"
            + "What can I do for you?";
    private static final String WELCOME_OUTPUT = LINE + "\n"
            + WELCOME_MESSAGE + "\n"
            + LINE + "\n";
    private static final String GOODBYE_OUTPUT = LINE + "\n"
            + "Bye. Hope to see you again soon!\n"
            + LINE + "\n";

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
        Files.writeString(dataFile, "T | 1 | read book" + FILE_NEWLINE);
        Tianyi tianyi = createTianyi("list\nbye\n", dataFile);

        tianyi.run();

        String listOutput = LINE + "\n"
                + "Here are the tasks in your list:\n"
                + "1.[T][X] read book\n"
                + LINE + "\n";
        assertEquals(WELCOME_OUTPUT + listOutput + GOODBYE_OUTPUT, getOutput());
    }

    @Test
    public void constructor_invalidStoredData_showsErrorAndStartsWithEmptyList()
            throws IOException {
        Path dataFile = tempDir.resolve("invalid.txt");
        Files.writeString(dataFile, "X | 0 | invalid" + FILE_NEWLINE);
        Tianyi tianyi = createTianyi("list\nbye\n", dataFile);

        tianyi.run();

        String loadErrorOutput = LINE + "\n"
                + "Oops! Unknown task type: X | 0 | invalid\n"
                + LINE + "\n";
        String emptyListOutput = LINE + "\n"
                + "No tasks found.\n"
                + LINE + "\n";
        assertEquals(loadErrorOutput + WELCOME_OUTPUT + emptyListOutput + GOODBYE_OUTPUT,
                getOutput());
    }

    @Test
    public void run_unknownCommand_showsErrorAndContinuesToBye() {
        Tianyi tianyi = createTianyi("abracadabra\nbye\n", tempDir.resolve("tasks.txt"));

        tianyi.run();

        String commandErrorOutput = LINE + "\n"
                + "Oops! I'm sorry, but I don't know what that means.\n"
                + LINE + "\n";
        assertEquals(WELCOME_OUTPUT + commandErrorOutput + GOODBYE_OUTPUT, getOutput());
    }

    @Test
    public void run_addCommand_savesTaskToConfiguredFile()
            throws IOException {
        Path dataFile = tempDir.resolve("nested/tasks.txt");
        Tianyi tianyi = createTianyi("todo read book\nbye\n", dataFile);

        tianyi.run();

        assertTrue(Files.isRegularFile(dataFile));
        assertEquals("T | 0 | read book" + FILE_NEWLINE, Files.readString(dataFile));
    }

    @Test
    public void getResponse_addThenList_returnsResponsesAndKeepsState() {
        Tianyi tianyi = createTianyi("", tempDir.resolve("tasks.txt"));

        Response addResponse = tianyi.getResponse("todo read book");
        Response listResponse = tianyi.getResponse("list");

        assertEquals("Got it. I've added this task:\n"
                + "  [T][ ] read book\n"
                + "Now you have 1 tasks in the list.", addResponse.getFullMessage());
        assertEquals("Here are the tasks in your list:\n"
                + "1.[T][ ] read book", listResponse.getFullMessage());
        assertFalse(addResponse.isError());
        assertFalse(listResponse.isError());
        assertFalse(addResponse.isExit());
        assertFalse(listResponse.isExit());
    }

    @Test
    public void getResponse_unknownCommand_returnsFormattedError() {
        Tianyi tianyi = createTianyi("", tempDir.resolve("tasks.txt"));

        Response response = tianyi.getResponse("abracadabra");

        assertEquals("Oops! I'm sorry, but I don't know what that means.", response.getMessage());
        assertTrue(response.isError());
        assertFalse(response.isExit());
    }

    @Test
    public void getResponse_blankInput_returnsEmptyString() {
        Tianyi tianyi = createTianyi("", tempDir.resolve("tasks.txt"));

        Response response = tianyi.getResponse("   ");

        assertEquals("", response.getMessage());
        assertFalse(response.isExit());
    }

    @Test
    public void getWelcomeMessage_always_returnsBannerAndGreeting() {
        Tianyi tianyi = createTianyi("", tempDir.resolve("tasks.txt"));

        String message = tianyi.getWelcomeMessage();

        assertEquals(WELCOME_MESSAGE, message);
    }

    @Test
    public void getResponse_taskNumberOutOfRange_returnsTryOnSeparateLine() {
        Tianyi tianyi = createTianyi("", tempDir.resolve("tasks.txt"));
        tianyi.getResponse("todo read book");

        Response response = tianyi.getResponse("mark 2");

        assertEquals("Oops! Task number 2 does not exist.\n"
                + "Please enter a number from 1 to 1.\n"
                + "Try: mark 1", response.getMessage());
        assertFalse(response.isExit());
    }

    @Test
    public void getResponse_byeCommand_returnsExitResponse() {
        Tianyi tianyi = createTianyi("", tempDir.resolve("tasks.txt"));

        Response response = tianyi.getResponse("bye");

        assertEquals("Bye. Hope to see you again soon!", response.getMessage());
        assertTrue(response.isExit());
    }

    @Test
    public void getResponse_successfulCommands_separatesHeaderFromBody() {
        Tianyi tianyi = createTianyi("", tempDir.resolve("tasks.txt"));
        Response added = tianyi.getResponse("todo read book: chapter 1");

        assertEquals("Got it. I've added this task:", added.getHeader());
        assertEquals("  [T][ ] read book: chapter 1\n"
                + "Now you have 1 tasks in the list.", added.getMessage());

        Response listed = tianyi.getResponse("list");
        assertEquals("Here are the tasks in your list:", listed.getHeader());
        assertEquals("1.[T][ ] read book: chapter 1", listed.getMessage());

        Response found = tianyi.getResponse("find book");
        assertEquals("Here are the matching tasks in your list:", found.getHeader());
        assertEquals(listed.getMessage(), found.getMessage());

        Response marked = tianyi.getResponse("mark 1");
        assertEquals("Nice! I've marked this task as done:", marked.getHeader());
        assertEquals("  [T][X] read book: chapter 1", marked.getMessage());

        Response unmarked = tianyi.getResponse("unmark 1");
        assertEquals("OK, I've marked this task as not done yet:", unmarked.getHeader());
        assertEquals("  [T][ ] read book: chapter 1", unmarked.getMessage());

        Response deleted = tianyi.getResponse("delete 1");
        assertEquals("Noted. I've removed this task:", deleted.getHeader());
        assertEquals("  [T][ ] read book: chapter 1\n"
                + "Now you have 0 tasks in the list.", deleted.getMessage());
    }

    @Test
    public void getResponse_help_preservesBlankLinesBetweenCommands() {
        Tianyi tianyi = createTianyi("", tempDir.resolve("tasks.txt"));

        Response response = tianyi.getResponse("help");

        assertEquals("Here is the list of commands:", response.getHeader());
        assertTrue(response.getMessage().contains("Example: bye\n\n[list]"));
        assertFalse(response.isError());
    }

    @Test
    public void getResponse_emptyResultsAndErrors_haveNoNormalHeader() {
        Tianyi tianyi = createTianyi("", tempDir.resolve("tasks.txt"));
        Response empty = tianyi.getResponse("list");

        assertEquals("", empty.getHeader());
        assertEquals("No tasks found.", empty.getMessage());
        assertFalse(empty.isError());

        Response error = tianyi.getResponse("todo");

        assertEquals("", error.getHeader());
        assertEquals("Oops! The argument of [todo] cannot be empty.\n"
                + "Try: todo borrow book", error.getMessage());
        assertTrue(error.isError());
        assertFalse(error.isExit());
    }

    private Tianyi createTianyi(String input, Path dataFile) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        return new Tianyi(dataFile.toString());
    }

    private String getOutput() {
        return output.toString(StandardCharsets.UTF_8)
                .replace("\r\n", "\n")
                .replace('\r', '\n');
    }
}
