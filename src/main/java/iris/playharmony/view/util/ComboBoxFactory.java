package iris.playharmony.view.util;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ComboBoxFactory {

    public static ComboBox comboBox(Object... objects) {
        ComboBox<Object> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(objects);
        if(objects.length > 0) comboBox.setValue(objects[0]);

        return comboBox;
    }

    public static Pane comboBoxLabeled(String text, Object... objects) {
        VBox panel = new VBox();

        Label label = new Label(text);
        ComboBox<Object> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(objects);
        if(objects.length > 0) comboBox.setValue(objects[0]);

        panel.getChildren().addAll(label, comboBox);

        return panel;
    }
}
