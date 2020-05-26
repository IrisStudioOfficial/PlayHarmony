package iris.playharmony.view.admin.user;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.User;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import javafx.scene.Node;

public class NewUserView extends UserFormView {

    public NewUserView() {
        super("New User");
    }

    @Override
    protected void initElements() {
        super.initElements();
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Add User", event -> createUser())
        };
    }

    private void createUser() {

        User user = getUserFromForm();

        if(user == null) {
            return;
        }

        if(DatabaseController.get().addUser(user)) {

            NavController.get().popView();
            UserListView userListView = NavController.get().getCurrentView();
            userListView.refresh();

        } else {
            AlertFactory.errorAlert("ERROR! User is already registered", "ERROR! User is already registered");
        }
    }
}
