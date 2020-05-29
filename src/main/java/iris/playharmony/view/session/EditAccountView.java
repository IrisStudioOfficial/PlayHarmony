package iris.playharmony.view.session;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.Role;
import iris.playharmony.model.User;
import iris.playharmony.view.main.LobbyView;
import iris.playharmony.view.template.FormTemplate;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TextFactory;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import iris.playharmony.session.Session;

public class EditAccountView extends FormTemplate {

    private TextField photo;
    private TextField name;
    private TextField surname;
    private TextField email;
    private TextField password;
    private TextField category;

    private User user;

    public EditAccountView() {
        super("Edit Account");
    }

    @Override
    protected void initElements() {
        this.user = Session.getSession().currentUser();
        photo = new TextField();

        add(TextFactory.label(user.getEmail().toString(), DefaultStyle.label()));

        add(TextFactory.label("Name", DefaultStyle.label()));
        add(name = TextFactory.textField(user.getName()));

        add(TextFactory.label("Surname", DefaultStyle.label()));
        add(surname = TextFactory.textField(user.getSurname()));

        add(TextFactory.label("Password", DefaultStyle.label()));
        add(password = TextFactory.textField(user.getPassword()));

        add(TextFactory.label("Category", DefaultStyle.label()));
        add(category = TextFactory.textField(user.getCategory()));
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Save Changes", event -> updateUser())
        };
    }

    private void updateUser() {
        User useredited = getUserFromForm();
        if(useredited != null) {
            try {
                if (DatabaseController.get().updateMyAccount(useredited,user.getEmail().toString())) {
                    NavController.get().pushView(new LobbyView());
                }
            } catch (Exception e) {
                AlertFactory.errorAlert("ERROR! User is already registered", "ERROR! User is already registered");
            }
        }
    }

    private User getUserFromForm() {
        user.setName(name.getText());
        user.setSurname(surname.getText());
        user.setPassword(password.getText());
        user.setCategory(category.getText());
        user.setRole(Role.STUDENT);

        return user;
    }
}