package iris.playharmony.view.admin.user;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.exceptions.RemoveUserException;
import iris.playharmony.model.ObservableUser;
import iris.playharmony.util.OnRefresh;
import iris.playharmony.view.template.ListTemplate;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;

import java.util.Comparator;

public class UserListView extends ListTemplate<ObservableUser> {

    public UserListView() {
        super("Users");
        init();
    }

    @Override
    protected void initElements() {

    }

    @Override
    protected void searchCommand() {
        if(searchField.getText().isEmpty())
            return;
        data = data.filtered(observableUser -> observableUser.getName().toLowerCase().contains(searchField.getText().toLowerCase()));
        TableFactory.updateTable(data, table);
        TableFactory.updatePagination(data, table, pagination);
    }

    @Override
    protected TableView initTable() {
        return TableFactory.table(data,
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Name", "name"),
                TableFactory.tableColumn("Surname", "surname"),
                TableFactory.tableColumn("Email", "Email"),
                TableFactory.tableColumn("Category", "Category"),
                TableFactory.tableColumn("Role", "Role")
        );
    }

    @Override
    protected Pagination initPagination() {
        return TableFactory.pagination(data, table);
    }

    @Override
    protected Comparator<ObservableUser> getComparator() {
        return Comparator.comparing(ObservableUser::getName);
    }

    @Override
    protected ObservableList<ObservableUser> getObservableData() {
        data = FXCollections.observableArrayList();
        new DatabaseController().getUsers().stream()
                .map(ObservableUser::from)
                .forEach(data::add);
        return data;
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Add User", e -> NavController.get().pushView(new NewUserView())),
                ButtonFactory.button("Remove User", this::removeUser),
                ButtonFactory.button("Update User", event -> updateUser())
        };
    }

    @OnRefresh
    @Override
    public void refresh() {
        data = getObservableData();
        TableFactory.updateTable(data, table);
        TableFactory.updatePagination(data, table, pagination);
    }

    private void removeUser(Event event) {
        event.consume();
        ObservableUser selection = (ObservableUser) table.getSelectionModel().getSelectedItem();
        if(selection == null)
            return;
        try {
            new DatabaseController().removeUser(selection.getEmail());
            refresh();
        } catch (RemoveUserException e) {
            AlertFactory.errorAlert("ERROR! Couldn't remove user", "ERROR! Couldn't remove user");
        }
    }

    private void updateUser() {
        ObservableUser selectedItem = (ObservableUser) table.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            NavController.get().pushView(new UpdateUserView(selectedItem));
        }
    }
}