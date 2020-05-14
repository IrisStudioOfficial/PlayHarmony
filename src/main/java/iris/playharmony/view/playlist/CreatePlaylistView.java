package iris.playharmony.view.playlist;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.User;
import iris.playharmony.view.main.UserView;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TextFactory;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CreatePlaylistView extends VBox {

    private TextField namePlayList;

    private static int SPACING = 15;
    
    public CreatePlaylistView(){
        super(SPACING);
        addElements();
        setPadding(new Insets(SPACING));
    }

    private void addElements() {
        add(TextFactory.label("Create Playlist", DefaultStyle.title()));
        add(namePlayList = TextFactory.textField("Name Of PlayList"));
        add(ButtonFactory.button("Create", e -> createPlayList()));
    }

    private Node add(Node node) {
        getChildren().add(node);
        return node;
    }

    private void createPlayList() {
        Playlist playlist = new Playlist(namePlayList.getText());

        // Use a default user to test the feature:
        User user = new DatabaseController().getUsers().stream().filter(i -> i.getName().equals("test")).findAny().orElse(null);

        if(new DatabaseController().addPlayList(playlist, user)) {
            NavController.get().popView();
            UserView userView = NavController.get().getCurrentView();
            userView.refresh();
        } else {
            AlertFactory.errorAlert("ERROR! PlayList is already registered", "Please introduce other name.");
        }
    }
}
