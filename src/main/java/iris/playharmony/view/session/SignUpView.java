package iris.playharmony.view.session;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.Email;
import iris.playharmony.model.Role;
import iris.playharmony.model.User;
import iris.playharmony.util.TypeUtils;
import iris.playharmony.view.main.LobbyView;
import iris.playharmony.view.template.FormTemplate;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TextFactory;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

import static java.util.Objects.nonNull;

public class SignUpView extends FormTemplate {

    private File photoFile;
    private TextField photo;
    private TextField name;
    private TextField surname;
    private TextField email;
    private TextField password;
    private TextField category;

    public SignUpView() {
        super("Sign Up");
    }

    @Override
    protected void initElements() {
        photo = new TextField();

        add(TextFactory.label("Name", DefaultStyle.label()));
        add(name = TextFactory.textField(""));
        add(TextFactory.label("Surname", DefaultStyle.label()));
        add(surname = TextFactory.textField(""));
        add(TextFactory.label("Email", DefaultStyle.label()));
        add(email = TextFactory.textField(""));
        add(TextFactory.label("Password", DefaultStyle.label()));
        add(password = TextFactory.textField(""));
        add(TextFactory.label("Category", DefaultStyle.label()));
        add(category = TextFactory.textField(""));
        add(TextFactory.label("Upload Image", DefaultStyle.label()));
        add(ButtonFactory.buttonWithLabeledResource(photo, "Photo", event -> uploadImage(photo)));
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Sign Up", event -> createUser())
        };
    }

    private void uploadImage(TextField textField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Search Image");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        photoFile = fileChooser.showOpenDialog(new Stage());
        textField.setText((photoFile == null) ? "" : photoFile.getAbsolutePath());
    }

    private void createUser() {
        User user = getUserFromForm();
        if(user != null) {
            try {
                if (DatabaseController.get().addUser(user)) {
                    NavController.get().pushView(new LobbyView());
                }
            } catch (Exception e) {
                AlertFactory.errorAlert("ERROR! User is already registered", "ERROR! User is already registered");
            }
        }
    }

    private User getUserFromForm() {
        User user = new User()
                .photo(photoFile)
                .name(name.getText())
                .surname(surname.getText())
                .category(category.getText())
                .role(Role.STUDENT)
                .mail(getEmail())
                .setPlayLists(new ArrayList<>())
                .setPassword(password.getText());

        if(allRequiredFieldsAreSet(user)) {
            return user;
        } else {
            AlertFactory.errorAlert("ERROR! User is incorrect", "ERROR! All required fields must be filled");
            return null;
        }
    }

    private Email getEmail() {
        if(Email.check(email.getText())) {
            return new Email(email.getText());
        } else {
            AlertFactory.errorAlert("ERROR! Email is incorrect", "ERROR! Email is incorrect");
            return null;
        }
    }

    private boolean allRequiredFieldsAreSet(User user) {
        return !isEmpty(photo) && !isEmpty(name) && !isEmpty(surname) && !isEmpty(category) && !isEmpty(email) && !isEmpty(password);
    }

    private boolean isEmpty(TextField textField) {
        return textField.getText().isEmpty();
    }
}
