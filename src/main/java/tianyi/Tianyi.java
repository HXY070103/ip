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
    private static final String DATA_FILE_PATH = "Data/tianyi.txt";

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final CommandParser parser;

    /**
     * Creates a Tianyi application that persists tasks at the specified path.
     *
     * @param filePath path of the file used to load and save tasks
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
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (TianyiException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Launches Tianyi using the default data file.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new Tianyi(DATA_FILE_PATH).run();
    }
}
