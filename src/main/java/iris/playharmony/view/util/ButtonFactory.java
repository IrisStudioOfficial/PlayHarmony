package iris.playharmony.view.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ButtonFactory {

    public static Button button(String buttonTitle, EventHandler<ActionEvent> event) {
        Button button = new Button(buttonTitle);
        button.setOnAction(event);
        return button;
    }
}
