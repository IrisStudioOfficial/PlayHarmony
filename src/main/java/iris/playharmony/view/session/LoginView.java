package iris.playharmony.view.session;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.Email;
import iris.playharmony.model.User;
import iris.playharmony.session.Session;
import iris.playharmony.view.template.FormTemplate;
import iris.playharmony.view.user.UserView;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TextFactory;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Objects;
import java.util.Optional;

public class LoginView extends FormTemplate {

    private TextField emailField;
    private PasswordField passwordField;

    public LoginView() {
        super("Login");
    }

    @Override
    protected void initElements() {
        add(TextFactory.label("Email", DefaultStyle.label()));
        add(emailField = TextFactory.textField(""));
        add(TextFactory.label("Password", DefaultStyle.label()));
        add(passwordField = new PasswordField());
    }

    @Override
    protected Node[] bottomButtonPanel() {

        Button login = ButtonFactory.button("Log in", event -> handleLogin());
        login.setStyle("-fx-background-color: green; -fx-text-fill: white;");

        Label label = new Label("Do not have an account yet?");

        Button signup = ButtonFactory.button("Sign up", event -> {
            NavController.get().pushView(new SignUpView());
        });

        return new Node[] { login, label, signup };
    }

    private void handleLogin() {

        final Email email = getEmail();

        if(email == null) {
            return;
        }

        final String password = passwordField.getText();

        if(password == null || password.trim().isEmpty()) {
            AlertFactory.errorAlert("ERROR: Login failed", "Password required.");
            return;
        }

        final Optional<User> retrievedUser = getUserByEmail(email);

        if(!retrievedUser.isPresent()) {
            AlertFactory.errorAlert("ERROR: Login failed", "User " + email + " does not exist.");
            return;
        }

        final User user = retrievedUser.get();

        if(!Objects.equals(user.getPassword(), password)) {
            AlertFactory.errorAlert("ERROR: Login failed", "Incorrect password. Please try again.");
            return;
        }

        Session.getSession().setCurrentUser(user);

        NavController.get().popView();
        NavController.get().pushView(new UserView());
    }

    private Optional<User> getUserByEmail(Email email) {
        return DatabaseController.get().getUsers().stream()
                .filter(user -> Objects.equals(user.getEmail(), email))
                .findAny();
    }

    private Email getEmail() {

        String text = emailField.getText();

        if(text == null || text.trim().isEmpty()) {
            AlertFactory.errorAlert("ERROR: Login failed", "Email required.");
            return null;
        }

        if(!Email.check(text)) {
            AlertFactory.errorAlert("ERROR: Login failed", "Email is incorrect. Please try again");
            return null;
        }

        return new Email(text);
    }
}
