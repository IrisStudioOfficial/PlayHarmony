package iris.playharmony.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;

public interface View {

    Font TITLE_FONT = new Font("Arial", 18);
    Font FIELD_FONT = new Font("Arial", 14);
    int ROWS_PER_PAGE = 20;

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

    default TableView table(ObservableList<?> data, TableColumn... columns) {
        TableView table = new TableView<>();

        table.setEditable(false);
        for (TableColumn column : columns) {
            table.getColumns().add(column);
        }
        table.getColumns().forEach(column -> ((TableColumn)column).setStyle("-fx-alignment: CENTER;"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(data);
        table.refresh();

        Pagination pagination = new Pagination((data.size() / ROWS_PER_PAGE + 1), 0);
        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * ROWS_PER_PAGE;
            int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, data.size());
            table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

            return new BorderPane(table);
        });

        add(table);

        return table;
    }

    default TableColumn tableColumn(String name, String id) {
        TableColumn column = new TableColumn(name);
        column.setCellValueFactory(new PropertyValueFactory<>(id));
        return column;
    }

    default TableColumn tableColumnPhoto(String name, String id) {
        TableColumn column = tableColumn(name, id);
        column.setPrefWidth(100);
        return column;
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

    default void updateTable(ObservableList<?> data, TableView table) {
        table.setItems(data);
        table.refresh();
    }
}
