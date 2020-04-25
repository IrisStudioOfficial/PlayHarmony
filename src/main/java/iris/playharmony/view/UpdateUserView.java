package iris.playharmony.view;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.exceptions.CreateUserException;
import iris.playharmony.exceptions.EmailException;
import iris.playharmony.exceptions.UpdateUserException;
import iris.playharmony.model.Email;
import iris.playharmony.model.ObservableUser;
import iris.playharmony.model.Role;
import iris.playharmony.model.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

import static iris.playharmony.util.TypeUtils.initSingleton;

public class UpdateUserView extends BorderPane {

    private static int SPACING = 15;
    private static Font TITLE_FONT = new Font("Arial", 18);
    private static Font FIELD_FONT = new Font("Arial", 14);
    private final ObservableUser user;

    private HeaderView headerView;
    private NavigationView navigationView;
    protected NavController navController;
    private FooterView footerView;

    public UpdateUserView(ObservableUser user) {
        headerView = new HeaderView();
        this.user = user;

        navigationView = new NavigationView();
        navigationView.setView(new UpdateUserViewNavigation(user, user.getEmail()));
        navController = new NavController(navigationView);

        footerView = new FooterView();

        setTop(headerView);
        setCenter(navigationView);
        setBottom(footerView);

        initSingleton(UpdateUserViewNavigation.class, navController);
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public class UpdateUserViewNavigation extends VBox {

        private File photoFile;

        private TextField photo = new TextField();
        private TextField name = new TextField();
        private TextField surname = new TextField();
        private TextField email = new TextField();
        private TextField category = new TextField();
        private ComboBox<Object> role = new ComboBox<>();
        ObservableUser user;
        String key;

        public UpdateUserViewNavigation(ObservableUser user, String key) {
            super(SPACING);
            this.user = user;
            this.key = key;
            name.setText(user.getName());
            surname.setText(user.getSurname());
            email.setText(user.getEmail());
            category.setText(user.getCategory());
            photo.setText("");
            add(title("Update User"));
            add(textFieldLabeled(name, "Name"));
            add(textFieldLabeled(surname, "Surname"));
            add(textFieldLabeled(email, "Email"));
            add(textFieldLabeled(category, "Category"));
            add(comboBoxLabeled(role, "Role", Role.STUDENT, Role.TEACHER, Role.ADMIN));
            add(buttonWithResult(photo,"Photo", "Upload Image", event -> uploadImage(photo)));
            add(button("Update User", event -> updateUser()));
            setPadding(new Insets(SPACING));
        }

        private Node add(Node node) {
            getChildren().add(node);

            return node;
        }

        private Label title(String text) {
            Label title = new Label(text);
            title.setFont(TITLE_FONT);
            return title;
        }

        private Node textFieldLabeled(TextField textField, String text) {
            VBox panel = new VBox();

            Label label = new Label(text);
            label.setFont(FIELD_FONT);

            panel.getChildren().addAll(label, textField);

            return panel;
        }

        private Node comboBoxLabeled(ComboBox<Object> comboBox, String text, Object... objects) {
            VBox panel = new VBox();

            Label label = new Label(text);
            label.setFont(FIELD_FONT);
            comboBox.getItems().addAll(objects);
            if(objects.length > 0) comboBox.setValue(objects[0]);

            panel.getChildren().addAll(label, comboBox);

            return panel;
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

        private Node button(String text, EventHandler<ActionEvent> event) {
            Button button = new Button(text);
            button.setOnAction(event);
            button.setBackground(new Background(new BackgroundFill(Color.rgb( 174, 214, 241 ), CornerRadii.EMPTY, Insets.EMPTY)));

            return button;
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
            try {
                User user = new User(photoFile, name.getText(), surname.getText(),
                        category.getText(), (Role) role.getValue(), new Email(email.getText()));
                try {
                    if(new DatabaseController().updateUser(user, key)) {
                        navController.clear();
                        navController.pushView(new UserListView().getNavigationView());
                    } else {
                        errorAlert("ERROR! Couldn't update user", "ERROR! Couldn't update user");
                    }
                } catch (UpdateUserException e) {
                    errorAlert("ERROR! User is incorrect", "ERROR! All required fields must be filled");
                }
            } catch (EmailException e) {
                errorAlert("ERROR! Email is incorrect", "ERROR! Email is incorrect");
            }
        }

        private void errorAlert(String title, String text) {
            Alert emailErrorDialog = new Alert(Alert.AlertType.ERROR);
            emailErrorDialog.setTitle(title);
            emailErrorDialog.setHeaderText(text);
            emailErrorDialog.initStyle(StageStyle.UTILITY);
            java.awt.Toolkit.getDefaultToolkit().beep();
            emailErrorDialog.showAndWait();
        }
    }
}
