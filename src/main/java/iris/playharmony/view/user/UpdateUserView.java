package iris.playharmony.view.user;

import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.exceptions.UpdateUserException;
import iris.playharmony.model.Email;
import iris.playharmony.model.ObservableUser;
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

import javax.xml.crypto.Data;
import java.io.File;
import java.util.ArrayList;

import static java.util.Objects.nonNull;

public class UpdateUserView extends VBox {

    private final ObservableUser user;

    private File photoFile;
    private TextField photo = new TextField();
    private TextField name = new TextField();
    private TextField surname = new TextField();
    private TextField email = new TextField();
    private TextField category = new TextField();
    private ComboBox<Object> role = new ComboBox<>();

    private static int SPACING = 15;

    public UpdateUserView(ObservableUser user) {
        super(SPACING);
        this.user = user;
        initElements();
        setPadding(new Insets(SPACING));
    }

    private void initElements() {
        add(TextFactory.label("Update User", DefaultStyle.title()));
        add(TextFactory.label("Name", DefaultStyle.label()));
        add(name = TextFactory.textField(user.getName()));
        add(TextFactory.label("Surname", DefaultStyle.label()));
        add(surname = TextFactory.textField(user.getSurname()));
        add(TextFactory.label("Email", DefaultStyle.label()));
        add(email = TextFactory.textField(user.getEmail()));
        add(TextFactory.label("Category", DefaultStyle.label()));
        add(category = TextFactory.textField(user.getCategory()));
        add(TextFactory.label("Role", DefaultStyle.label()));
        add(role = TextFactory.comboBox(Role.values()));
        add(ButtonFactory.buttonWithLabeledResource(photo, "Upload Image", event -> uploadImage(photo)));
        add(ButtonFactory.button("Update User", event -> updateUser()));
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

    private void updateUser() {

        User user = getUserFromForm();

        DatabaseController db = new DatabaseController();

        if(db.updateUser(user, user.getEmail().toString())) {

            NavController.get().popView();
            UserListView userListView = NavController.get().getCurrentView();
            userListView.refresh();

        } else {
            AlertFactory.errorAlert("ERROR! Couldn't update user", "ERROR! Couldn't update user");
        }
    }

    private User getUserFromForm() {

        Email email = getEmail();

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
        return TypeUtils.getAllFieldValues(user).stream().map(String::valueOf).allMatch(field -> nonNull(field) && !field.isEmpty());
    }

    private Email getEmail() {

        String text = email.getText();

        if(!Email.check(text)) {
            AlertFactory.errorAlert("ERROR! Email is incorrect", "ERROR! Email is incorrect");
        }

        return new Email(text);
    }
}
