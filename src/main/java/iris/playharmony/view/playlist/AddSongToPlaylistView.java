package iris.playharmony.view.playlist;

import iris.playharmony.controller.NavController;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.view.*;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import static iris.playharmony.util.TypeUtils.initSingleton;

public class AddSongToPlaylistView extends BorderPane {

    private static int SPACING = 15;
    private NavController navController;

    public AddSongToPlaylistView(Playlist playlist) {
        HeaderView headerView = new HeaderView();

        NavigationView navigationView = new NavigationView();
        navigationView.setView(new AddSongToPlaylistViewNavigation(playlist));
        navController = new NavController(navigationView);

        FooterView footerView = new FooterView();

        setTop(headerView);
        setCenter(navigationView);
        setBottom(footerView);

        initSingleton(AddSongToPlaylistViewNavigation.class, navController);
    }

    public class AddSongToPlaylistViewNavigation extends VBox implements View {

        private Playlist playlist;

        public AddSongToPlaylistViewNavigation(Playlist playlist) {
            super(SPACING);
            this.playlist = playlist;
            initElements();
            setPadding(new Insets(SPACING));
        }

        private void initElements() {
            title(playlist.getName());
            for (Song song : playlist.getSongList()) {
                button(song.getName(), null);
            }
            button("Add Song", event -> addSong());
        }

        private void addSong() {
            navController.clear();
            //navController.pushView(new AddSongToPlaylistSongListView().getNavigationView());
        }
    }
}
