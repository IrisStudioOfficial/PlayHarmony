package iris.playharmony.view.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class TableFactory {

    private static int ROWS_PER_PAGE = 20;

    public static Pagination pagination(ObservableList<?> data, TableView table) {
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

        return pagination;
    }

    public static TableView table(ObservableList<?> data, TableColumn... columns) {
        TableView table = new TableView<>();

        table.setEditable(false);
        for (TableColumn column : columns) {
            table.getColumns().add(column);
        }
        table.getColumns().forEach(column -> ((TableColumn)column).setStyle("-fx-alignment: CENTER;"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(data);
        table.refresh();

        return table;
    }

    public static TableColumn tableColumn(String name, String id) {
        TableColumn column = new TableColumn(name);
        column.setCellValueFactory(new PropertyValueFactory<>(id));
        return column;
    }

    public static TableColumn tableColumnPhoto(String name, String id, int size) {
        TableColumn column = tableColumn(name, id);
        column.setPrefWidth(size);
        return column;
    }

    public static void updatePagination(ObservableList<?> data, TableView table, Pagination pagination) {
        int rest = data.size() % ROWS_PER_PAGE;
        pagination.setPageCount((rest != 0) ? (data.size() / ROWS_PER_PAGE) + 1 : data.size() / ROWS_PER_PAGE);
        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * ROWS_PER_PAGE;
            int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, data.size());
            table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

            return new BorderPane(table);
        });
    }

    public static void updateTable(ObservableList<?> data, TableView table) {
        table.setItems(data);
        table.refresh();
    }
}
