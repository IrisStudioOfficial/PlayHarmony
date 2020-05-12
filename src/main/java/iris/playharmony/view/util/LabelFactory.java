package iris.playharmony.view.util;

import javafx.scene.control.Label;

public class LabelFactory {

    public static Label label(String text) {
        Label label = new Label(text);
        return label;
    }
}
