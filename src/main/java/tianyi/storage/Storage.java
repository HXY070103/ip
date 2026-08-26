package tianyi.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import tianyi.task.Task;

/**
 * Loads tasks from a data file and saves tasks back to it.
 */
public class Storage {
    private final File file;
    private final DataParser parser;

    public Storage(String filePath) {
        file = new File(filePath);
        parser = new DataParser();
    }

    public List<Task> load() throws StorageException {
        if (!file.exists()) {
            return new ArrayList<>();
        }

        List<Task> tasks = new ArrayList<>();

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                tasks.add(parser.parse(sc.nextLine()));
            }
        } catch (IOException e) {
            throw new StorageException("Unable to load tasks from " + file.getPath() + ".");
        }

        return tasks;
    }

    public void save(List<Task> tasks) throws StorageException {
        File dataFolder = file.getParentFile();

        if (dataFolder != null && !dataFolder.exists() && !dataFolder.mkdirs()) {
            throw new StorageException("Unable to create the data folder.");
        }

        try (FileWriter fw = new FileWriter(file)) {
            for (Task task : tasks) {
                fw.write(task.getData() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new StorageException("Unable to save tasks to " + file.getPath() + ".");
        }
    }
}
