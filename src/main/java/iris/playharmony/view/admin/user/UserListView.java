package iris.playharmony.view.admin.user;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.exceptions.RemoveUserException;
import iris.playharmony.model.ObservableUser;
import iris.playharmony.view.template.ListTemplate;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;

import java.util.Comparator;

public class UserListView extends ListTemplate<ObservableUser> {

    public UserListView() {
        super("Users");
    }

    @Override
    protected Comparator<ObservableUser> initComparator() {
        return Comparator.comparing(ObservableUser::getName);
    }

    @Override
    protected ObservableList<ObservableUser> getData() {
        ObservableList<ObservableUser> observableUsers = FXCollections.observableArrayList();
        new DatabaseController().getUsers().stream()
                .map(ObservableUser::from)
                .forEach(observableUsers::add);
        return observableUsers;
    }

    @Override
    protected String fieldToFilter(ObservableUser user) {
        return user.getName();
    }

    @Override
    protected TableColumn[] initTable() {
        return new TableColumn[] {
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Name", "name"),
                TableFactory.tableColumn("Surname", "surname"),
                TableFactory.tableColumn("Email", "Email"),
                TableFactory.tableColumn("Category", "Category"),
                TableFactory.tableColumn("Role", "Role")
        };
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Add User", e -> NavController.get().pushView(new NewUserView())),
                ButtonFactory.button("Remove User", this::removeUser),
                ButtonFactory.button("Update User", event -> updateUser())
        };
    }

    private void removeUser(Event event) {
        event.consume();
        ObservableUser selection = getSelectedItem();
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
        ObservableUser selectedItem = getSelectedItem();
        if(selectedItem != null) {
            NavController.get().pushView(new UpdateUserView(selectedItem));
        }
    }
}