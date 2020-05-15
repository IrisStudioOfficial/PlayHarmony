package iris.playharmony.view.util;

import iris.playharmony.util.ImageFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ButtonFactory {

    public static Button button(String buttonTitle, EventHandler<ActionEvent> event) {
        Button button = new Button(buttonTitle);
        button.setOnAction(event);
        return button;
    }

    public static Node buttonWithLabeledResource(TextField textField, String buttonText, EventHandler<ActionEvent> event) {
        HBox panel = new HBox();

        textField.setDisable(true);

        Button button = new Button(buttonText);
        button.setOnAction(event);

        panel.getChildren().addAll(textField, button);

        return panel;
    }

    public static Button imageButton(String imagePath) {
        Button button = new Button();
        button.setGraphic(new ImageView(ImageFactory.loadFromFile(imagePath)));
        return button;
    }
}
