package tianyi;

import tianyi.command.Command;
import tianyi.command.CommandParser;
import tianyi.storage.Storage;
import tianyi.task.TaskList;
import tianyi.ui.Ui;

/**
 * Runs the Tianyi chatbot application.
 */
public class Tianyi {
    /**
     * Default location used to persist tasks in console mode.
     */
    private static final String DATA_FILE_PATH = "Data/tianyi.txt";

    /**
     * Banner and greeting displayed when Tianyi starts.
     */
    private static final String WELCOME_MESSAGE =
            " _____ _                   _\n"
                    + "|_   _(_) __ _ _ __  _   _(_)\n"
                    + "  | | | |/ _` | '_ \\| | | | |\n"
                    + "  | | | | (_| | | | | |_| | |\n"
                    + "  |_| |_|\\__,_|_| |_|\\__, |_|\n"
                    + "                     |___/\n"
                    + "Hello! I'm Tianyi.\n"
                    + "What can I do for you?";

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final CommandParser parser;

    /**
     * Creates a Tianyi application that persists tasks at the specified path.
     *
     * @param filePath Path of the file used to load and save tasks.
     */
    public Tianyi(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        parser = new CommandParser();

        TaskList loadedTasks;

        try {
            loadedTasks = new TaskList(storage.load());
        } catch (TianyiException e) {
            ui.showError(e.getMessage());
            loadedTasks = new TaskList();
        }

        tasks = loadedTasks;
    }

    /**
     * Returns Tianyi's welcome message.
     *
     * @return Welcome banner and greeting.
     */
    public String getWelcomeMessage() {
        return WELCOME_MESSAGE;
    }

    /**
     * Processes one user command and returns a response suitable for display.
     *
     * @param input Complete command entered by the user.
     * @return Command response, or an empty string when the input is blank.
     */
    public String getResponse(String input) {
        if (input.isBlank()) {
            return "";
        }

        try {
            Command command = parser.parse(input, tasks);
            return command.execute(tasks, storage);
        } catch (TianyiException e) {
            return "Oops! " + e.getMessage();
        }
    }

    /**
     * Starts the command loop and processes input until the user exits or input ends.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit && ui.hasNextCommand()) {
            String fullCommand = ui.readCommand();

            if (fullCommand.isBlank()) {
                continue;
            }

            try {
                Command command = parser.parse(fullCommand, tasks);
                String response = command.execute(tasks, storage);
                ui.showResponse(response);
                isExit = command.isExit();
            } catch (TianyiException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Launches Tianyi using the default data file.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Tianyi(DATA_FILE_PATH).run();
    }
}
