package tianyi.ui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Creates a dialog box containing the supplied message and display image.
     *
     * @param text Message displayed in the dialog box.
     * @param image Display image for the speaker.
     */
    private DialogBox(String text, Image image) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(image);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Creates a dialog box for a user message.
     *
     * @param text Message text.
     * @param image User display image.
     * @return Dialog box aligned for the user.
     */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, image);
    }

    /**
     * Creates a dialog box for a Tianyi response.
     *
     * @param text Response text.
     * @param image Tianyi display image.
     * @return Dialog box aligned for Tianyi.
     */
    public static DialogBox getTianyiDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);
        dialogBox.flip();
        return dialogBox;
    }

    /**
     * Creates a Tianyi dialog box styled for displaying the welcome banner.
     *
     * @param text Welcome message text.
     * @param image Tianyi display image.
     * @return Dialog box with monospaced banner styling.
     */
    public static DialogBox getWelcomeDialog(String text, Image image) {
        DialogBox dialogBox = getTianyiDialog(text, image);
        dialogBox.dialog.getStyleClass().add("welcome-label");
        return dialogBox;
    }
}
