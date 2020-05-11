package iris.playharmony.view.song;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;

import java.util.Comparator;

public abstract class SongListView extends VBox {
    protected TableView songsTable = new TableView<>();
    protected TextField searchField = new TextField();
    ObservableList<ObservableSong> data = FXCollections.observableArrayList();
    protected static final int SPACING = 15;
    protected static final Font TITLE_FONT = new Font("Arial", 18);
    protected static final Font FIELD_FONT = new Font("Arial", 14);
    protected static final int ROWS_PER_PAGE = 20;

    public SongListView() {
        super(SPACING);
    }

    protected TableView initializeTableView() {
        songsTable.setEditable(false);
        initializeColumns();
        updateTableViewData();
        return songsTable;
    }

    protected TableView updateTableViewData() {
        data = getSongs(new Comparator<ObservableSong>() {
            @Override
            public int compare(ObservableSong o1, ObservableSong o2) {
                return o1.title().get().compareTo(o2.title().get());
            }
        });
        songsTable.setItems(data);
        songsTable.refresh();
        return songsTable;
    }

    protected Node add(Node node) {
        getChildren().add(node);
        return node;
    }

    protected ObservableList<ObservableSong> getSongs(Comparator<ObservableSong> comparator) {
        data = FXCollections.observableArrayList();
        new DatabaseController()
                .getSongs()
                .stream()
                .map(ObservableSong::from)
                .sorted(comparator)
                .forEach(data::add);
        return data;
    }

    protected Pagination getPagination() {
        Pagination pagination = new Pagination((data.size() / ROWS_PER_PAGE + 1), 0);
        pagination.setPageFactory(this::createPage);
        return pagination;
    }

    protected Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, data.size());
        songsTable.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(songsTable);
    }

    protected TableView initializeColumns() {
        TableColumn imageColumn = new TableColumn("Photo");
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
        imageColumn.setPrefWidth(100);
        TableColumn titleColumn = new TableColumn("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn authorColumn = new TableColumn("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn dateColumn = new TableColumn("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn pathColumn = new TableColumn("Path");
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("Path"));
        songsTable.getColumns().addAll(imageColumn, titleColumn, authorColumn, dateColumn, pathColumn);
        songsTable.getColumns().forEach(column -> ((TableColumn) column).setStyle("-fx-alignment: CENTER;"));
        songsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return songsTable;
    }


    protected Node button(String text, EventHandler<ActionEvent> event) {
        Button button = new Button(text);
        button.setOnAction(event);
        button.setBackground(new Background(new BackgroundFill(Color.rgb(174, 214, 241), CornerRadii.EMPTY, Insets.EMPTY)));

        return button;
    }


    protected void errorAlert(String title, String text) {
        Alert emailErrorDialog = new Alert(Alert.AlertType.ERROR);
        emailErrorDialog.setTitle(title);
        emailErrorDialog.setHeaderText(text);
        emailErrorDialog.initStyle(StageStyle.UTILITY);
        java.awt.Toolkit.getDefaultToolkit().beep();
        emailErrorDialog.showAndWait();
    }

    protected Node searchForm() {
        HBox searchRow = new HBox();
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        Region padding = new Region();
        padding.setPrefWidth(5);
        searchField.setOnAction(event -> searchCommand());
        searchRow.getChildren().add(region);
        searchRow.getChildren().add(searchField);
        searchRow.getChildren().add(button("Search", event -> searchCommand()));

        return searchRow;
    }

    protected void searchCommand() {
        updateTableViewData();
        if(searchField.getText().isEmpty()) {
            return;
        }
        data = data.filtered(observableSong -> observableSong.getTitle().toLowerCase().contains(searchField.getText().toLowerCase()));
        songsTable.setItems(data);
        songsTable.refresh();
    }

    protected void removeSong(Event event) {
        event.consume();
        ObservableSong selection = (ObservableSong) songsTable.getSelectionModel().getSelectedItem();
        if (selection == null)
            return;
        if (!new DatabaseController().deleteSong(new Song().setTitle(selection.getTitle())))
            errorAlert("ERROR! Couldn't remove song", "ERROR! Couldn't remove song");
        updateTableViewData();
    }

    protected Node getBottomButtonPanel() {
        Region padding = new Region();
        padding.setPrefWidth(5);

        return new HBox(button("Refresh", event -> {
            updateTableViewData();
        }));
    }

    protected Label title(String text) {
        Label title = new Label(text);
        title.setFont(TITLE_FONT);
        return title;
    }
}
