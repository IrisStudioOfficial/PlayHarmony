package iris.playharmony.view.session;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.exceptions.CreateUserException;
import iris.playharmony.exceptions.EmailException;
import iris.playharmony.model.Email;
import iris.playharmony.model.Role;
import iris.playharmony.model.User;
import iris.playharmony.util.OnRefresh;
import iris.playharmony.util.TypeUtils;
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

    @Override
    public void refresh() {

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
        try {
            User user = new User(photoFile, name.getText(), surname.getText(),
                    category.getText(), Role.STUDENT, new Email(email.getText()), new ArrayList<>());
            try {
                if(new DatabaseController().addUser(user)) {
                    NavController.get().popView();
                    TypeUtils.callAnnotatedMethod(NavController.get().getCurrentView(), OnRefresh.class);
                } else {
                    AlertFactory.errorAlert("ERROR! User is already registered", "ERROR! User is already registered");
                }
            } catch (CreateUserException e) {
                AlertFactory.errorAlert("ERROR! User is incorrect", "ERROR! All required fields must be filled");
            }
        } catch (EmailException e) {
            AlertFactory.errorAlert("ERROR! Email is incorrect", "ERROR! Email is incorrect");
        }
    }
}
