package iris.playharmony.view.util;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class TextFieldFactory {

    public static TextField textField() {
        TextField textField = new TextField();
        return textField;
    }

    public static Pane textFieldLabeled(String text) {
        VBox panel = new VBox();

        Label label = new Label(text);
        TextField textField = new TextField();

        panel.getChildren().addAll(label, textField);

        return panel;
    }
}
