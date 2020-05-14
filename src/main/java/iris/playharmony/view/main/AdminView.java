package iris.playharmony.view.main;

import iris.playharmony.controller.NavController;
import iris.playharmony.view.song.AdminSongListView;
import iris.playharmony.view.user.UserListView;
import iris.playharmony.view.util.ButtonFactory;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class AdminView extends VBox {

    private static int SPACING = 15;

    public AdminView() {
        super(SPACING);
        initElements();
        setPadding(new Insets(SPACING));
    }

    private void initElements() {
        add(ButtonFactory.button("Users", event -> NavController.get().pushView(new UserListView())));
        add(ButtonFactory.button("Songs", event -> NavController.get().pushView(new AdminSongListView())));
    }

    private Node add(Node node) {
        getChildren().add(node);
        return node;
    }
}
