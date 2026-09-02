package tianyi.ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import tianyi.Tianyi;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    private static final Duration EXIT_DELAY = Duration.seconds(1);

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Tianyi tianyi;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser-square.png"));
    private Image tianyiImage = new Image(this.getClass().getResourceAsStream("/images/DaTianyi-square-v2.png"));

    /**
     * Binds the scroll position to the dialog container height.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Tianyi instance used to process user input.
     *
     * @param tianyi Tianyi application instance.
     */
    public void setTianyi(Tianyi tianyi) {
        this.tianyi = tianyi;

        dialogContainer.getChildren().add(
                DialogBox.getWelcomeDialog(tianyi.getWelcomeMessage(), tianyiImage));
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Tianyi's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        if (input.isBlank()) {
            return;
        }

        String response = tianyi.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getTianyiDialog(response, tianyiImage)
        );
        userInput.clear();

        if (input.trim().equalsIgnoreCase("bye")) {
            PauseTransition exitDelay = new PauseTransition(EXIT_DELAY);
            exitDelay.setOnFinished(event -> Platform.exit());
            exitDelay.play();
        }
    }
}
