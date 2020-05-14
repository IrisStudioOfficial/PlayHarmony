package iris.playharmony.view.playlist;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.User;
import iris.playharmony.view.song.AdminSongListView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;

public class CreatePlaylistView extends VBox {
    private static int SPACING = 15;
    private static Font TITLE_FONT = new Font("Arial", 18);
    private static Font FIELD_FONT = new Font("Arial", 14);
    private TextField namePlayList = new TextField();

    
    public CreatePlaylistView(){
        super(SPACING);

        add(title("Create A PlayList"));
        add(textFieldLabeled(namePlayList, "Name Of PlayList"));

        add(button("Create", event -> createPlayList()));

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

    private Node button(String text, EventHandler<ActionEvent> event) {
        Button button = new Button(text);
        button.setOnAction(event);
        button.setBackground(new Background(new BackgroundFill(Color.rgb( 174, 214, 241 ), CornerRadii.EMPTY, Insets.EMPTY)));

        return button;
    }

    private void createPlayList() {
        Playlist playlist = new Playlist(namePlayList.getText());

        // Use a default user to test the feature:
        User user = new DatabaseController().getUsers().stream().filter(i -> i.getName().equals("test")).findAny().orElse(null);

        if(new DatabaseController().addPlayList(playlist, user)) {
            NavController.get().popView();
        } else {
            errorMessage("ERROR! PlayList is already registered", "Please introduce other name.");
        }
    }

    private void errorMessage(String title, String text) {
        Alert errorDialog = new Alert(Alert.AlertType.ERROR);
        errorDialog.setTitle(title);
        errorDialog.setHeaderText(text);
        errorDialog.initStyle(StageStyle.UTILITY);
        java.awt.Toolkit.getDefaultToolkit().beep();
        errorDialog.showAndWait();
    }
}
