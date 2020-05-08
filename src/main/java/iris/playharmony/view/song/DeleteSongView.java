package iris.playharmony.view.song;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.Song;
import iris.playharmony.view.user.UserListView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;

public class DeleteSongView extends VBox {
    private static int SPACING = 15;
    private static Font TITLE_FONT = new Font("Arial", 20);
    private static Font MESSAGE_FONT = new Font("Arial", 15);

    public DeleteSongView(Song song) {
        super(SPACING);

        add(title("Delete Song"));
        add(message("Do you want to delete the song " + song.getTitle() + "?"));
        add(button("Delete Song", event -> deleteSong(song)));
        add(button("Cancel", event -> cancelDeleteSong()));
    }

    private void deleteSong(Song song){
        if(new DatabaseController().deleteSong(song)) {
            NavController.get().pushView(new SongListView());
        } else {
            errorAlert("ERROR! Song couldn't be deleted", "ERROR! Song couldn't be deleted");
        }
    }

    private void cancelDeleteSong(){
        NavController.get().popView();
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

    private Label message(String text) {
        Label title = new Label(text);
        title.setFont(MESSAGE_FONT);
        return title;
    }

    private Node button(String text, EventHandler<ActionEvent> event) {
        Button button = new Button(text);
        button.setOnAction(event);
        button.setBackground(new Background(new BackgroundFill(Color.rgb( 174, 214, 241 ), CornerRadii.EMPTY, Insets.EMPTY)));

        return button;
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
