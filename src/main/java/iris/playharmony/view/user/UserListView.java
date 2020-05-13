package iris.playharmony.view.user;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.exceptions.RemoveUserException;
import iris.playharmony.model.ObservableUser;
import iris.playharmony.view.playlist.CreatePlaylistView;
import iris.playharmony.view.song.AdminSongListView;
import iris.playharmony.view.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class UserListView extends VBox {

    ObservableList<ObservableUser> data;

    private TableView usersTable;
    private Pagination pagination;

    private static int SPACING = 15;

    public UserListView() {
        super(SPACING);
        data = getDBData();
        initElements();
        setPadding(new Insets(SPACING));
    }

    private void initElements() {
        add(getTitleRow());

        add(usersTable = TableFactory.table(data,
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Name", "name"),
                TableFactory.tableColumn("Surname", "surname"),
                TableFactory.tableColumn("Email", "Email"),
                TableFactory.tableColumn("Category", "Category"),
                TableFactory.tableColumn("Role", "Role")
        ));

        add(pagination = TableFactory.pagination(data, usersTable));

        add(getBottomButtonPanel());
    }

    private void add(Node node) {
        getChildren().add(node);
    }

    private Node getTitleRow() {
        HBox titleRow = new HBox(TextFactory.label("Users", DefaultStyle.title()));
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        titleRow.getChildren().add(region);
        titleRow.getChildren().add(ButtonFactory.button("Add User", event -> {
            NavController.get().pushView(new NewUserView());
        }));

        titleRow.getChildren().add(ButtonFactory.button("List Songs", event -> {
            NavController.get().pushView(new AdminSongListView());
        }));

        titleRow.getChildren().add(ButtonFactory.button("Create PlayList", event -> {
            NavController.get().pushView(new CreatePlaylistView());
        }));

        return titleRow;
    }

    private Node getBottomButtonPanel() {
        Region padding = new Region();
        padding.setPrefWidth(5);
        HBox bottomButtonPanel = new HBox(
                ButtonFactory.button("Remove User", this::removeUser),
                padding,
                ButtonFactory.button("Update User", event -> updateUser())
        );
        return bottomButtonPanel;
    }

    private void updateUser() {
        ObservableUser selectedItem = (ObservableUser) usersTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            NavController.get().pushView(new UpdateUserView(selectedItem));
        }
    }

    private void removeUser(Event event) {
        event.consume();
        ObservableUser selection = (ObservableUser) usersTable.getSelectionModel().getSelectedItem();
        if(selection == null)
            return;
        try {
            new DatabaseController().removeUser(selection.getEmail());
            update();
        } catch (RemoveUserException e) {
            AlertFactory.errorAlert("ERROR! Couldn't remove user", "ERROR! Couldn't remove user");
        }
    }

    private ObservableList<ObservableUser> getDBData() {
        data = FXCollections.observableArrayList();
        new DatabaseController().getUsers().stream()
                .map(ObservableUser::from)
                .forEach(data::add);
        return data;
    }

    public void update() {
        data = getDBData();
        TableFactory.updateTable(data, usersTable);
        TableFactory.updatePagination(data, usersTable, pagination);
    }
}
