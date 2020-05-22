package iris.playharmony.view.main;

import iris.playharmony.controller.NavController;
import iris.playharmony.view.session.SignUpView;
import iris.playharmony.view.user.UserView;
import iris.playharmony.view.user.playlist.FavouriteSongListView;
import iris.playharmony.view.util.ButtonFactory;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class LobbyView extends VBox {

    private static int SPACING = 15;

    public LobbyView() {
        super(SPACING);
        initElements();
        setPadding(new Insets(SPACING));
    }

    private void initElements() {
        add(ButtonFactory.button("Admin", event -> NavController.get().pushView(new AdminView())));
        add(ButtonFactory.button("User", event -> NavController.get().pushView(new UserView())));
        add(ButtonFactory.button("Sign up", event -> NavController.get().pushView(new SignUpView())));
        add(ButtonFactory.button("Favourites", event -> NavController.get().pushView(new FavouriteSongListView())));
    }

    private Node add(Node node) {
        getChildren().add(node);
        return node;
    }
}
