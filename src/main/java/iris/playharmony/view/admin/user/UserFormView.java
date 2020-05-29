package iris.playharmony.view.admin.user;

import iris.playharmony.controller.handler.PathHandler;
import iris.playharmony.model.Email;
import iris.playharmony.model.Role;
import iris.playharmony.model.User;
import iris.playharmony.util.CircleImage;
import iris.playharmony.util.ImageFactory;
import iris.playharmony.util.Resources;
import iris.playharmony.util.TypeUtils;
import iris.playharmony.view.template.FormTemplate;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TextFactory;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

import static java.util.Objects.nonNull;

public abstract class UserFormView extends FormTemplate {

    private File photoFile;
    private CircleImage photoView;
    private TextField photoFileField = new TextField();
    protected TextField name = new TextField();
    TextField surname = new TextField();
    protected TextField email = new TextField();
    PasswordField passwordField = new PasswordField();
    TextField category = new TextField();
    protected ComboBox<Object> role = new ComboBox<>();

    UserFormView(String title) {
        super(title);
    }

    UserFormView(String title, Object baseElement) {
        super(title, baseElement);
    }

    @Override
    protected void initElements() {
        add(photoView = new CircleImage());
        photoView.setRadius(128.0);
        add(TextFactory.label("Name", DefaultStyle.label()));
        add(name = new TextField());
        add(TextFactory.label("Surname", DefaultStyle.label()));
        add(surname = new TextField());
        add(TextFactory.label("Email", DefaultStyle.label()));
        add(email = new TextField());
        add(TextFactory.label("Password", DefaultStyle.label()));
        add(passwordField = new PasswordField());
        add(TextFactory.label("Category", DefaultStyle.label()));
        add(category = new TextField());
        add(TextFactory.label("Role", DefaultStyle.label()));
        add(role = TextFactory.comboBox(Role.values()));
        photoFileField = new TextField();
        add(ButtonFactory.buttonWithLabeledResource(photoFileField, "Upload Image", event -> uploadImage(photoFileField)));
        setPhoto(null);
    }

    void setPhoto(Image image) {
        if(image == null) {
            photoView.setImage(ImageFactory.loadFromFile(Resources.get(PathHandler.DEFAULT_PHOTO_PATH)));
        } else {
            photoView.setImage(image);
        }
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

        setPhoto(ImageFactory.loadFromFile(photoFile.getAbsolutePath()));
    }

    User getUserFromForm() {

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

        user.setPassword(passwordField.getText());

        if(notAllFieldsAreSet(user)) {
            AlertFactory.errorAlert("ERROR! User is incorrect", "ERROR! All required fields must be filled");
        }

        return user;
    }

    private boolean notAllFieldsAreSet(User user) {
        return !TypeUtils.getAllFieldValues(user).stream().map(String::valueOf).allMatch(field -> nonNull(field) && !field.trim().isEmpty());
    }

    protected Email getEmail() {

        String text = email.getText();

        if(!Email.check(text)) {
            AlertFactory.errorAlert("ERROR! Email is incorrect", "ERROR! Email is invalid");
            return null;
        }

        return new Email(text);
    }
}
