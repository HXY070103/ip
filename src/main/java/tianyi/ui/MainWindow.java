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
import tianyi.Response;
import tianyi.Tianyi;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    /**
     * Delay that keeps the goodbye response visible before the application closes.
     */
    private static final Duration EXIT_DELAY = Duration.seconds(1);

    /**
     * Pixel tolerance for treating the scrollbar as already at the bottom.
     */
    private static final double BOTTOM_TOLERANCE = 2;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button jumpToLatest;

    private Tianyi tianyi;

    private final Image userImage =
            new Image(getClass().getResourceAsStream("/images/DaUser-square.png"));

    private final Image tianyiImage =
            new Image(getClass().getResourceAsStream("/images/DaTianyi-square-v2.png"));

    /**
     * Creates the controller loaded by the main-window FXML file.
     */
    public MainWindow() {
    }

    /**
     * Shows the shortcut whenever the latest reply is below the viewport.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) ->
                updateJumpButton());
        scrollPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) ->
                updateJumpButton());
        dialogContainer.layoutBoundsProperty().addListener((observable, oldValue, newValue) ->
                updateJumpButton());
    }

    /**
     * Injects the Tianyi instance used to process user input.
     *
     * @param tianyi Tianyi application instance.
     */
    public void setTianyi(Tianyi tianyi) {
        this.tianyi = tianyi;

        dialogContainer.getChildren().add(
                DialogBox.createWelcomeDialog(
                        "Hello! I'm Tianyi.\n"
                                + "What can I do for you?",
                        tianyiImage
                )
        );
    }

    /**
     * Appends the user's input and Tianyi's response to the dialog container.
     * Clears the input, shows the latest reply, and exits after a goodbye response.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        if (input.isBlank()) {
            return;
        }

        Response response = tianyi.getResponse(input);

        appendDialogs(input, response);
        userInput.clear();

        showLatestReply();

        if (response.isExit()) {
            scheduleExit();
        }
    }

    /**
     * Adds the user's message and the appropriately styled response to the conversation.
     *
     * @param input Command entered by the user.
     * @param response Result of processing the command.
     */
    private void appendDialogs(String input, Response response) {
        dialogContainer.getChildren().addAll(
                DialogBox.createUserDialog(input, userImage),
                response.isError()
                        ? DialogBox.createErrorDialog(response.getMessage(), tianyiImage)
                        : DialogBox.createTianyiDialog(response.getMessage(), tianyiImage)
        );
    }

    /**
     * Updates message sizes before scrolling to the latest reply.
     */
    private void showLatestReply() {
        scrollPane.applyCss();
        scrollPane.layout();

        scrollToLatest();
    }

    /**
     * Schedules application shutdown after allowing time to read the goodbye response.
     */
    private void scheduleExit() {
        PauseTransition exitDelay = new PauseTransition(EXIT_DELAY);
        exitDelay.setOnFinished(event -> Platform.exit());

        exitDelay.play();
    }

    /**
     * Updates shortcut visibility after scrolling or a change in available space.
     */
    private void updateJumpButton() {
        jumpToLatest.setVisible(!isAtBottom());
    }

    /**
     * Returns the content height that lies outside the visible viewport.
     */
    private double getScrollableHeight() {
        double contentHeight = dialogContainer.getLayoutBounds().getHeight();
        double viewportHeight = scrollPane.getViewportBounds().getHeight();

        return Math.max(0, contentHeight - viewportHeight);
    }

    /**
     * Reports whether the user is viewing the end of the conversation.
     */
    private boolean isAtBottom() {
        double distanceToBottom = (1 - scrollPane.getVvalue()) * getScrollableHeight();

        return distanceToBottom <= BOTTOM_TOLERANCE;
    }

    /**
     * Shows the latest reply and returns keyboard focus to the command field.
     */
    @FXML
    private void scrollToLatest() {
        scrollPane.setVvalue(1);
        jumpToLatest.setVisible(false);

        userInput.requestFocus();
    }
}
