package iris.playharmony.view.session;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.Email;
import iris.playharmony.model.Role;
import iris.playharmony.model.User;
import iris.playharmony.view.main.LobbyView;
import iris.playharmony.view.template.FormTemplate;
import iris.playharmony.view.util.*;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.io.File;
import java.util.ArrayList;

public class EditAccountView extends FormTemplate {

    private File photoFile;
    private TextField photo;
    private TextField name;
    private TextField surname;
    private TextField email;
    private TextField password;
    private TextField category;

    public EditAccountView() {
        super("Edit Account");
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
        photo = ButtonFactory.buttonWithLabeledResource(this, "Photo", event -> {
            photoFile = FileFactory.loadPhoto();
            photo.setText(photoFile.getAbsolutePath());
        });
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Sign Up", event -> createUser())
        };
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