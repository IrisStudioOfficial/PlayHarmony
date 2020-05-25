package iris.playharmony.view.admin.user;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.ObservableUser;
import iris.playharmony.model.User;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;

public class UpdateUserView extends UserFormView {

    private final ObservableUser user;

    public UpdateUserView(ObservableUser user) {
        this.user = user;
        name.setText(user.getName());
        surname.setText(user.getSurname());
        email.setText(user.getEmail());
        category.setText(user.getCategory());
    }

    @Override
    protected void initElements() {
        super.initElements();
        add(ButtonFactory.button("Update User", event -> updateUser()));
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
