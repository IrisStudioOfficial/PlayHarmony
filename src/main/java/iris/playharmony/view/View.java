package iris.playharmony.view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;

public interface View {

    Font TITLE_FONT = new Font("Arial", 18);
    Font FIELD_FONT = new Font("Arial", 14);

    ObservableList<Node> getChildren();

    default Node add(Node node) {
        getChildren().add(node);

        return node;
    }

    default void button(String buttonTitle, EventHandler<ActionEvent> event) {
        Button button = new Button(buttonTitle);
        button.setOnAction(event);
        button.setBackground(new Background(new BackgroundFill(Color.rgb( 174, 214, 241 ), CornerRadii.EMPTY, Insets.EMPTY)));
        add(button);
    }

    default void comboBoxLabeled(ComboBox<Object> comboBox, String text, Object... objects) {
        VBox panel = new VBox();

        Label label = new Label(text);
        label.setFont(FIELD_FONT);
        comboBox.getItems().addAll(objects);
        if(objects.length > 0) comboBox.setValue(objects[0]);

        panel.getChildren().addAll(label, comboBox);

        add(panel);
    }

    default void errorAlert(String title, String text) {
        Alert emailErrorDialog = new Alert(Alert.AlertType.ERROR);
        emailErrorDialog.setTitle(title);
        emailErrorDialog.setHeaderText(text);
        emailErrorDialog.initStyle(StageStyle.UTILITY);
        java.awt.Toolkit.getDefaultToolkit().beep();
        emailErrorDialog.showAndWait();
    }

    default void textFieldLabeled(TextField textField, String text) {
        VBox panel = new VBox();

        Label label = new Label(text);
        label.setFont(FIELD_FONT);

        panel.getChildren().addAll(label, textField);

        add(panel);
    }

    default void title(String text) {
        Label title = new Label(text);
        title.setFont(TITLE_FONT);
        add(title);
    }
}
