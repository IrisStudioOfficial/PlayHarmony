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

    int SPACING = 15;
    Font TITLE_FONT = new Font("Arial", 18);
    Font FIELD_FONT = new Font("Arial", 14);
    int ROWS_PER_PAGE = 20;

    ObservableList<Node> getChildren();

    default Node add(Node node) {
        getChildren().add(node);

        return node;
    }

    default Button button(String buttonTitle, EventHandler<ActionEvent> event) {
        Button button = new Button(buttonTitle);
        button.setOnAction(event);
        button.setBackground(new Background(new BackgroundFill(Color.rgb( 174, 214, 241 ), CornerRadii.EMPTY, Insets.EMPTY)));
        return (Button) add(button);
    }

    default ComboBox comboBoxLabeled(ComboBox<Object> comboBox, String text, Object... objects) {
        VBox panel = new VBox();

        Label label = new Label(text);
        label.setFont(FIELD_FONT);
        comboBox.getItems().addAll(objects);
        if(objects.length > 0) comboBox.setValue(objects[0]);

        panel.getChildren().addAll(label, comboBox);

        return (ComboBox) add(panel);
    }

    default void errorAlert(String title, String text) {
        Alert emailErrorDialog = new Alert(Alert.AlertType.ERROR);
        emailErrorDialog.setTitle(title);
        emailErrorDialog.setHeaderText(text);
        emailErrorDialog.initStyle(StageStyle.UTILITY);
        java.awt.Toolkit.getDefaultToolkit().beep();
        emailErrorDialog.showAndWait();
    }

    default Label label(String text) {
        Label label = new Label(text);
        label.setFont(FIELD_FONT);
        return (Label) add(label);
    }

    default Pagination pagination(ObservableList<?> data, TableView table) {
        Pagination pagination;
        if(data.size() == 0) {
            pagination = new Pagination(1, 0);
        } else {
            int rest = data.size() % ROWS_PER_PAGE;
            pagination = new Pagination((rest != 0) ? (data.size() / ROWS_PER_PAGE) + 1 : data.size() / ROWS_PER_PAGE, 0);
        }
        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * ROWS_PER_PAGE;
            int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, data.size());
            table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

            return new BorderPane(table);
        });

        return (Pagination) add(pagination);
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

        return (TableView) add(table);
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

    default Pane textFieldLabeled(TextField textField, String text) {
        VBox panel = new VBox();

        Label label = new Label(text);
        label.setFont(FIELD_FONT);

        panel.getChildren().addAll(label, textField);

        return (Pane) add(panel);
    }

    default Label title(String text) {
        Label title = new Label(text);
        title.setFont(TITLE_FONT);
        return (Label) add(title);
    }

    default void updatePagination(ObservableList<?> data, TableView table, Pagination pagination) {
        int rest = data.size() % ROWS_PER_PAGE;
        pagination.setPageCount((rest != 0) ? (data.size() / ROWS_PER_PAGE) + 1 : data.size() / ROWS_PER_PAGE);
        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * ROWS_PER_PAGE;
            int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, data.size());
            table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

            return new BorderPane(table);
        });
    }

    default void updateTable(ObservableList<?> data, TableView table) {
        table.setItems(data);
        table.refresh();
    }
}
