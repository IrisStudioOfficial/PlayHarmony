package iris.playharmony.view.admin.user;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.ObservableUser;
import iris.playharmony.model.User;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import javafx.scene.Node;

public class UpdateUserView extends UserFormView {

    private ObservableUser user;

    public UpdateUserView(Object baseElement) {
        super("Update User", baseElement);
    }

    @Override
    protected void initBaseElement(Object user) {
        this.user = (ObservableUser) user;
    }

    @Override
    protected void initElements() {
        super.initElements();
        name.setText(this.user.getName());
        surname.setText(this.user.getSurname());
        email.setText(this.user.getEmail());
        category.setText(this.user.getCategory());
        passwordField.setText(this.user.getPassword());
        setPhoto(user.getPhoto().getImage());
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Update User", event -> updateUser())
        };
    }

    private void updateUser() {

        User user = getUserFromForm();

        if(user == null) {
            return;
        }

        if(DatabaseController.get().updateUser(user, user.getEmail().toString())) {

            NavController.get().popView();
            UserListView userListView = NavController.get().getCurrentView();
            userListView.refresh();

        } else {
            AlertFactory.errorAlert("ERROR! Failed to update user", "ERROR! Failed to update user");
        }
    }
}
