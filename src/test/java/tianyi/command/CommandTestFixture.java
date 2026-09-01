package tianyi.command;

import java.util.List;

import tianyi.storage.Storage;
import tianyi.task.Task;
import tianyi.ui.Ui;

/**
 * Provides recording test doubles shared by command unit tests.
 */
final class CommandTestFixture {
    private CommandTestFixture() {
    }

    /**
     * Records the latest response or goodbye request without printing it.
     */
    static class RecordingUi extends Ui {
        protected String response;
        protected boolean goodbyeShown;

        @Override
        public void showResponse(String response) {
            this.response = response;
        }

        @Override
        public void showGoodbye() {
            goodbyeShown = true;
        }
    }

    /**
     * Records the task snapshot passed to save without writing a file.
     */
    static class RecordingStorage extends Storage {
        protected List<Task> savedTasks;

        RecordingStorage() {
            super("unused");
        }

        @Override
        public void save(List<Task> tasks) {
            savedTasks = List.copyOf(tasks);
        }
    }
}
