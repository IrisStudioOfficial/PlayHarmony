package iris.playharmony.view.util;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TextFactory {

    public static ComboBox<Object> comboBox(Object[] objects) {
        ComboBox<Object> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(objects);
        if(objects.length > 0) {
            comboBox.setValue(objects[0]);
        }

        return comboBox;
    }

    public static Label label(String text, Style labelStyle) {
        Label label = new Label(text);
        label.setStyle(labelStyle.toString());
        return label;
    }

    public static TextField textField(String text) {
        TextField textField = new TextField();
        textField.setText(text);
        return textField;
    }
}
