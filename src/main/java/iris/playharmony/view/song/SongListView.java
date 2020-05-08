package iris.playharmony.view.song;

import iris.playharmony.controller.NavController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Song;
import iris.playharmony.view.FooterView;
import iris.playharmony.view.HeaderView;
import iris.playharmony.view.NavigationView;
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

import java.io.File;

public class SongListView extends VBox {
    private static int SPACING = 15;
    private static Font TITLE_FONT = new Font("Arial", 18);
    private static Font FIELD_FONT = new Font("Arial", 14);
    private static final int ROWS_PER_PAGE = 20;
    private TableView songsTable = new TableView<>();

    ObservableList<ObservableSong> data = FXCollections.observableArrayList();

    public SongListView() {
        super(SPACING);
        add(getTitleRow());
        initializeTableView();
        add(getPagination());
        add(getBottomButtonPanel());
        setPadding(new Insets(SPACING));
    }

    private Node add(Node node) {
        getChildren().add(node);
        return node;
    }

    private Node getTitleRow() {
        HBox titleRow = new HBox(title("Songs"));
        return titleRow;
    }

    private Node getBottomButtonPanel() {
        Region padding = new Region();
        Region padding2 = new Region();
        padding.setPrefWidth(5);
        padding2.setPrefWidth(5);
        HBox bottomButtonPanel = new HBox(button("Button1", event -> {
            event.consume();
        }),
                padding,
                button("Button2", event ->{
                    event.consume();
                }),
                padding2,
                button("Button3", event -> {
                    event.consume();
                }));
        return bottomButtonPanel;
    }

    private Label title(String text) {
        Label title = new Label(text);
        title.setFont(TITLE_FONT);
        return title;
    }


    private Node textFieldLabeled(TextField textField, String text) {
        VBox panel = new VBox();

        Label label = new Label(text);
        label.setFont(FIELD_FONT);

        panel.getChildren().addAll(label, textField);

        return panel;
    }

    private TableView initializeTableView() {
        songsTable.setEditable(false);
        initializeColumns();
        updateTableViewData();
        return songsTable;
    }

    private TableView updateTableViewData() {
        data = getSongs();
        songsTable.setItems(data);
        songsTable.refresh();
        return songsTable;
    }

    private ObservableList<ObservableSong> getSongs() {
        data = FXCollections.observableArrayList();
        mockSongs()
                .stream()
                .forEach(data::add);
        return data;
    }

    private Pagination getPagination() {
        Pagination pagination = new Pagination((data.size() / ROWS_PER_PAGE + 1), 0);
        pagination.setPageFactory(this::createPage);
        return pagination;
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, data.size());
        songsTable.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(songsTable);
    }

    private TableView initializeColumns() {
        TableColumn imageColumn = new TableColumn("Photo");
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
        imageColumn.setPrefWidth(100);
        TableColumn titleColumn = new TableColumn("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn authorColumn = new TableColumn("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn dateColumn = new TableColumn("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        songsTable.getColumns().addAll(imageColumn, titleColumn, authorColumn, dateColumn);
        songsTable.getColumns().forEach(column -> ((TableColumn)column).setStyle("-fx-alignment: CENTER;"));
        songsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return songsTable;
    }

    private ObservableList<ObservableSong> mockSongs() {
        ObservableList<ObservableSong> users = FXCollections.observableArrayList();
        users.add(ObservableSong.from(new Song()
                .setPhoto(new File("C:\\Users\\omark\\OneDrive\\Pictures\\eva.jpg"))
                .setAuthor("Lady Gaga")
                .setDate("21-10-2010")
                .setTitle("Poker face")
        ));
        users.add(ObservableSong.from(new Song()
                .setPhoto(new File("C:\\Users\\omark\\OneDrive\\Pictures\\eva.jpg"))
                .setAuthor("Lady Gaga")
                .setDate("21-10-2010")
                .setTitle("Poker face")
        ));
        users.add(ObservableSong.from(new Song()
                .setPhoto(new File("C:\\Users\\omark\\OneDrive\\Pictures\\eva.jpg"))
                .setAuthor("Lady Gaga")
                .setDate("21-10-2010")
                .setTitle("Poker face")
        ));
        users.add(ObservableSong.from(new Song()
                .setPhoto(new File("C:\\Users\\omark\\OneDrive\\Pictures\\eva.jpg"))
                .setAuthor("Lady Gaga")
                .setDate("21-10-2010")
                .setTitle("Poker face")
        ));
        return users;
    }

    private Node button(String text, EventHandler<ActionEvent> event) {
        Button button = new Button(text);
        button.setOnAction(event);
        button.setBackground(new Background(new BackgroundFill(Color.rgb( 174, 214, 241 ), CornerRadii.EMPTY, Insets.EMPTY)));

        return button;
    }


    private void errorAlert(String title, String text) {
        Alert emailErrorDialog = new Alert(Alert.AlertType.ERROR);
        emailErrorDialog.setTitle(title);
        emailErrorDialog.setHeaderText(text);
        emailErrorDialog.initStyle(StageStyle.UTILITY);
        java.awt.Toolkit.getDefaultToolkit().beep();
        emailErrorDialog.showAndWait();
    }
}


