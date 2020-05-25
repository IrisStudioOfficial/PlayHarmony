package iris.playharmony.view.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

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

    public static Node searchField(TextField searchField, EventHandler<ActionEvent> event) {
        HBox searchRow = new HBox();
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        Region padding = new Region();
        padding.setPrefWidth(5);

        searchField.setOnAction(event);
        searchRow.getChildren().add(region);
        searchRow.getChildren().add(searchField);
        searchRow.getChildren().add(ButtonFactory.button("Search", event));

        return searchRow;
    }
}
