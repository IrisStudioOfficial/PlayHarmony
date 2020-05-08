package iris.playharmony.view.song;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.exceptions.CreateUserException;
import iris.playharmony.exceptions.EmailException;
import iris.playharmony.model.Email;
import iris.playharmony.model.Role;
import iris.playharmony.model.Song;
import iris.playharmony.model.User;
import iris.playharmony.view.FooterView;
import iris.playharmony.view.HeaderView;
import iris.playharmony.view.NavigationView;
import iris.playharmony.view.user.NewUserView;
import iris.playharmony.view.user.UserListView;
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

public class NewSongView extends VBox {
    private static int SPACING = 15;
    private static Font TITLE_FONT = new Font("Arial", 18);
    private static Font FIELD_FONT = new Font("Arial", 14);
    private TextField title = new TextField();
    private TextField author = new TextField();
    private TextField pathPhoto = new TextField();
    private File photoFile;
    private TextField dateDay = new TextField();
    private TextField dateMonth = new TextField();
    private TextField dateYear = new TextField();
    private TextField pathFile = new TextField();
    private File songFile;

    public NewSongView() {
        super(SPACING);

        add(title("Add Song"));
        add(textFieldLabeled(title, "Title"));
        add(textFieldLabeled(author, "Author"));

        add(textFieldLabeled(dateDay, "Day"));
        add(textFieldLabeled(dateMonth, "Month"));
        add(textFieldLabeled(dateYear, "Year"));

        add(buttonWithResult(pathPhoto,"Photo", "Upload Image", event -> uploadImage(pathPhoto)));
        add(buttonWithResult(pathFile,"Song File", "Upload Song", event -> uploadSong(pathFile)));

        add(button("Add Song", event -> createSong()));

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

    private void uploadSong(TextField textField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Search Song");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3")
        );

        songFile = fileChooser.showOpenDialog(new Stage());
        textField.setText((songFile == null) ? "" : songFile.getAbsolutePath());
    }


    private void createSong() {
        Song song = new Song(title.getText(),author.getText(),photoFile,dateDay.getText() + "-" + dateMonth.getText() +"-" + dateYear.getText(),pathFile.getText());
        if(new DatabaseController().addSong(song)) {
            NavController.get().clear();
            NavController.get().pushView(new SongListView());
        } else {
            errorAlert("ERROR! User is already registered", "ERROR! User is already registered");
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

