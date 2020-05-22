package iris.playharmony.view.admin.user;

import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.exceptions.CreateUserException;
import iris.playharmony.model.Email;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Role;
import iris.playharmony.model.User;
import iris.playharmony.util.TypeUtils;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TextFactory;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

import static java.util.Objects.nonNull;

public class NewUserView extends VBox {

    private File photoFile;
    private TextField photo = new TextField();
    private TextField name = new TextField();
    private TextField surname = new TextField();
    private TextField email = new TextField();
    private TextField category = new TextField();
    private ComboBox<Object> role = new ComboBox<>();

    private static int SPACING = 15;

    public NewUserView() {
        super(SPACING);
        initElements();
        setPadding(new Insets(SPACING));
    }

    private void initElements() {
        add(TextFactory.label("Add User", DefaultStyle.title()));
        add(TextFactory.label("Name", DefaultStyle.label()));
        add(name = TextFactory.textField(""));
        add(TextFactory.label("Surname", DefaultStyle.label()));
        add(surname = TextFactory.textField(""));
        add(TextFactory.label("Email", DefaultStyle.label()));
        add(email = TextFactory.textField(""));
        add(TextFactory.label("Category", DefaultStyle.label()));
        add(category = TextFactory.textField(""));
        add(TextFactory.label("Role", DefaultStyle.label()));
        add(role = TextFactory.comboBox(Role.values()));
        add(TextFactory.label("Upload Image", DefaultStyle.label()));
        add(ButtonFactory.buttonWithLabeledResource(photo, "Photo", event -> uploadImage(photo)));
        add(ButtonFactory.button("Add User", event -> createUser()));
    }

    private void add(Node node) {
        getChildren().add(node);
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

        if(user == null) {
            return;
        }

        DatabaseController db = new DatabaseController();

        if(db.addUser(user)) {

            NavController.get().popView();
            UserListView userListView = NavController.get().getCurrentView();
            userListView.refresh();

        } else {
            AlertFactory.errorAlert("ERROR! User is already registered", "ERROR! User is already registered");
        }
    }

    private User getUserFromForm() {

        Email email = getEmail();

        if(email == null) {
            return null;
        }

        User user = new User(
                photoFile,
                name.getText(),
                surname.getText(),
                category.getText(),
                (Role) role.getValue(),
                email,
                new ArrayList<>());

        if(notAllFieldsAreSet(user)) {
            AlertFactory.errorAlert("ERROR! User is incorrect", "ERROR! All required fields must be filled");
        }

        return user;
    }

    private boolean notAllFieldsAreSet(User user) {
        return !TypeUtils.getAllFieldValues(user).stream().map(String::valueOf).allMatch(field -> nonNull(field) && !field.trim().isEmpty());
    }

    private Email getEmail() {

        String text = email.getText();

        if(!Email.check(text)) {
            AlertFactory.errorAlert("ERROR! Email is incorrect", "ERROR! Email is incorrect");
            return null;
        }

        return new Email(text);
    }
}
