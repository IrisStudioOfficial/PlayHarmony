package iris.playharmony.view.util;

import iris.playharmony.util.ImageFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class ButtonFactory {

    public static Button button(String buttonTitle, EventHandler<ActionEvent> event) {
        Button button = new Button(buttonTitle);
        button.setOnAction(event);
        return button;
    }

    public static TextField buttonWithLabeledResource(Pane window, String buttonText, EventHandler<ActionEvent> event) {
        HBox panel = new HBox();

        TextField textField = new TextField();
        textField.setEditable(false);

        Button button = new Button(buttonText);
        button.setOnAction(event);

        panel.getChildren().addAll(textField, button);
        window.getChildren().add(panel);

        return textField;
    }

    public static Button imageButton(String imagePath) {
        Button button = new Button();
        button.setGraphic(new ImageView(ImageFactory.loadFromFile(imagePath)));
        return button;
    }
}
