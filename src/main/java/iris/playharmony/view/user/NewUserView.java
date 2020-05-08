package iris.playharmony.view.user;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.exceptions.CreateUserException;
import iris.playharmony.exceptions.EmailException;
import iris.playharmony.model.Email;
import iris.playharmony.model.Role;
import iris.playharmony.model.User;
import iris.playharmony.view.FooterView;
import iris.playharmony.view.HeaderView;
import iris.playharmony.view.NavigationView;
import iris.playharmony.view.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class NewUserView extends VBox implements View {

    private File photoFile;
    private static int SPACING = 15;
    private TextField photo = new TextField();
    private TextField name = new TextField();
    private TextField surname = new TextField();
    private TextField email = new TextField();
    private TextField category = new TextField();
    private ComboBox<Object> role = new ComboBox<>();

    public NewUserView() {
        super(SPACING);

        title("Add User");
        textFieldLabeled(name, "Name");
        textFieldLabeled(surname, "Surname");
        textFieldLabeled(email, "Email");
        textFieldLabeled(category, "Category");
        comboBoxLabeled(role, "Role", Role.STUDENT, Role.TEACHER, Role.ADMIN);
        add(buttonWithResult(photo,"Photo", "Upload Image", event -> uploadImage(photo)));
        button("Add User", event -> createUser());

        setPadding(new Insets(SPACING));
    }

    private Node buttonWithResult(TextField textField, String labelText, String buttonText, EventHandler<ActionEvent> event) {
        Label photoText = new Label(labelText);
        photoText.setFont(FIELD_FONT);

        HBox panel = new HBox();

        textField.setDisable(true);

        Button button = new Button(buttonText);
        button.setOnAction(event);
        button.setBackground(new Background(new BackgroundFill(Color.rgb( 174, 214, 241 ), CornerRadii.EMPTY, Insets.EMPTY)));

        panel.getChildren().addAll(textField, button);

        return panel;
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
                    category.getText(), (Role) role.getValue(), new Email(email.getText()));
            try {
                if(new DatabaseController().addUser(user)) {
                    NavController.get().popView();
                } else {
                    errorAlert("ERROR! User is already registered", "ERROR! User is already registered");
                }
            } catch (CreateUserException e) {
                errorAlert("ERROR! User is incorrect", "ERROR! All required fields must be filled");
            }
        } catch (EmailException e) {
            errorAlert("ERROR! Email is incorrect", "ERROR! Email is incorrect");
        }
    }
}
