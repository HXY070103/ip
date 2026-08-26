/**
 * Runs the Tianyi chatbot application.
 */

public class Tianyi {
    private static final String DATA_FILE_PATH = "Data/tianyi.txt";

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final CommandParser parser;

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

    public static void main(String[] args) {
        new Tianyi(DATA_FILE_PATH).run();
    }
}
