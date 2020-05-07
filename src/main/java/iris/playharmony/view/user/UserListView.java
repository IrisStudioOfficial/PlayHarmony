package iris.playharmony.view.user;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.exceptions.RemoveUserException;
import iris.playharmony.model.ObservableUser;
import iris.playharmony.view.FooterView;
import iris.playharmony.view.HeaderView;
import iris.playharmony.view.NavigationView;
import iris.playharmony.view.song.NewSongView;
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

import java.util.Optional;

import static iris.playharmony.util.TypeUtils.initSingleton;

public class UserListView extends BorderPane {
    private static int SPACING = 15;
    private static Font TITLE_FONT = new Font("Arial", 18);
    private static final int ROWS_PER_PAGE = 20;

    private HeaderView headerView;
    private NavigationView navigationView;
    private NavController navController;
    private FooterView footerView;

    public UserListView() {
        headerView = new HeaderView();

        navigationView = new NavigationView();
        navigationView.setView(new UserListViewNavigation());
        navController = new NavController(navigationView);

        footerView = new FooterView();

        setTop(headerView);
        setCenter(navigationView);
        setBottom(footerView);

        initSingleton(NewUserView.UserViewNavigation.class, navController);
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public class UserListViewNavigation extends VBox {

        private TextField name = new TextField();
        private TextField surname = new TextField();
        private TextField email = new TextField();
        private TextField category = new TextField();
        private TableView usersTable = new TableView<>();
        private ComboBox<Object> role = new ComboBox<>();

        ObservableList<ObservableUser> data = FXCollections.observableArrayList();

        public UserListViewNavigation() {
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
            HBox titleRow = new HBox(title("Users"));
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            titleRow.getChildren().add(region);
            titleRow.getChildren().add(button("Add User", event -> {
                navController.clear();
                navController.pushView(new NewUserView().getNavigationView());
            }));

            titleRow.getChildren().add(button("Add Song", event -> {
                navController.clear();
                navController.pushView(new NewSongView().getNavigationView());
            }));

            return titleRow;
        }

        private Node getBottomButtonPanel() {
            Region padding = new Region();
            padding.setPrefWidth(5);
            HBox bottomButtonPanel = new HBox(button("Remove User", this::removeUser),
                    padding,
                    button("Update User", event ->{
                        navController.clear();
                        ObservableUser selectedItem = (ObservableUser) usersTable.getSelectionModel().getSelectedItem();
                        if(selectedItem != null)
                            navController.pushView(new UpdateUserView(selectedItem).getNavigationView());
                    }));
            return bottomButtonPanel;
        }
        private Label title(String text) {
            Label title = new Label(text);
            title.setFont(TITLE_FONT);
            return title;
        }

        private void removeUser(Event event) {
            event.consume();
            ObservableUser selection = (ObservableUser) usersTable.getSelectionModel().getSelectedItem();
            if(selection == null)
                return;
            try {
                if(confirmAlert("Remove User", "Do you want to delete the user?"))
                    new DatabaseController().removeUser(selection.getEmail());
            } catch (RemoveUserException e) {
                errorAlert("ERROR! Couldn't remove user", "ERROR! Couldn't remove user");
            }
            updateTableViewData();
        }

        private TableView initializeTableView() {
            usersTable.setEditable(false);
            initializeColumns(usersTable);
            updateTableViewData();
            return usersTable;
        }

        private TableView updateTableViewData() {
            data = getDBData();
            usersTable.setItems(data);
            usersTable.refresh();
            return usersTable;
        }

        private ObservableList<ObservableUser> getDBData() {
            data = FXCollections.observableArrayList();
            new DatabaseController().getUsers().stream()
                    .map(ObservableUser::from)
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
            usersTable.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

            return new BorderPane(usersTable);
        }
        private TableView initializeColumns(TableView tableView) {
            TableColumn imageColumn = new TableColumn("Photo");
            imageColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
            imageColumn.setPrefWidth(100);
            TableColumn nameColumn = new TableColumn("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            TableColumn surnameColumn = new TableColumn("Surname");
            surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
            TableColumn emailColumn = new TableColumn("Email");
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            TableColumn categoryColumn = new TableColumn("Category");
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
            TableColumn roleColumn = new TableColumn("Role");
            roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

            usersTable.getColumns().addAll(imageColumn, nameColumn, surnameColumn, emailColumn, categoryColumn, roleColumn);
            usersTable.getColumns().forEach(column -> ((TableColumn)column).setStyle("-fx-alignment: CENTER;"));
            usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            return usersTable;
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

        private boolean confirmAlert(String title, String text) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(title);
            alert.setHeaderText(text);

            java.awt.Toolkit.getDefaultToolkit().beep();
            Optional<ButtonType> result = alert.showAndWait();
            return result.get() == ButtonType.OK;
        }
    }
}
