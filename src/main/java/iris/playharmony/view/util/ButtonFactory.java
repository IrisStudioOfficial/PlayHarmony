package iris.playharmony.view.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.io.File;

public class ButtonFactory {

    public static Button button(String buttonTitle, EventHandler<ActionEvent> event) {
        Button button = new Button(buttonTitle);
        button.setOnAction(event);
        return button;
    }

    public static Button imageButton(String imagePath) {
        Button button = new Button();
        button.setGraphic(new ImageView(imagePath));
        return button;
    }
}
