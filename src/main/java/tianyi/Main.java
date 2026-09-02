package tianyi;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tianyi.ui.MainWindow;

/**
 * A GUI for Tianyi using FXML.
 */
public class Main extends Application {
    private static final String DATA_FILE_PATH = "Data/tianyi.txt";

    private Tianyi tianyi = new Tianyi(DATA_FILE_PATH);

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(500);
            fxmlLoader.<MainWindow>getController().setTianyi(tianyi);
            stage.setTitle("Tianyi");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
