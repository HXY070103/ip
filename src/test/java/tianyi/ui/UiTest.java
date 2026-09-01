package tianyi.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests console input and exact output formatting of {@link Ui}.
 */
public class UiTest {
    private static final String LINE =
            "____________________________________________________________";
    private static final String NEWLINE = System.lineSeparator();

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
    public void readCommand_multipleLines_readsInOrderUntilEnd() {
        setInput("todo read book\nbye\n");
        Ui ui = new Ui();

        assertTrue(ui.hasNextCommand());
        assertEquals("todo read book", ui.readCommand());
        assertTrue(ui.hasNextCommand());
        assertEquals("bye", ui.readCommand());
        assertFalse(ui.hasNextCommand());
    }

    @Test
    public void readCommand_blankLine_returnsEmptyString() {
        setInput("\n");
        Ui ui = new Ui();

        assertEquals("", ui.readCommand());
    }

    @Test
    public void showWelcome_always_printsBannerAndGreetingBetweenLines() {
        Ui ui = new Ui();

        ui.showWelcome();

        assertEquals(LINE + NEWLINE
                + " _____ _                   _\n"
                + "|_   _(_) __ _ _ __  _   _(_)\n"
                + "  | | | |/ _` | '_ \\| | | | |\n"
                + "  | | | | (_| | | | | |_| | |\n"
                + "  |_| |_|\\__,_|_| |_|\\__, |_|\n"
                + "                     |___/\n"
                + "Hello! I'm Tianyi.\n"
                + "What can I do for you?" + NEWLINE
                + LINE + NEWLINE, getOutput());
    }

    @Test
    public void showResponse_multilineResponse_printsContentBetweenLines() {
        Ui ui = new Ui();

        ui.showResponse("first line\nsecond line");

        assertEquals(LINE + NEWLINE
                + "first line\nsecond line" + NEWLINE
                + LINE + NEWLINE, getOutput());
    }

    @Test
    public void showError_message_printsPrefixedMessageBetweenLines() {
        Ui ui = new Ui();

        ui.showError("Something went wrong.");

        assertEquals(LINE + NEWLINE
                + "Oops! Something went wrong." + NEWLINE
                + LINE + NEWLINE, getOutput());
    }

    @Test
    public void showGoodbye_always_printsFarewellBetweenLines() {
        Ui ui = new Ui();

        ui.showGoodbye();

        assertEquals(LINE + NEWLINE
                + "Bye. Hope to see you again soon!" + NEWLINE
                + LINE + NEWLINE, getOutput());
    }

    @Test
    public void showLine_always_printsSingleSeparator() {
        Ui ui = new Ui();

        ui.showLine();

        assertEquals(LINE + NEWLINE, getOutput());
    }

    private void setInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    }

    private String getOutput() {
        return output.toString(StandardCharsets.UTF_8);
    }
}
