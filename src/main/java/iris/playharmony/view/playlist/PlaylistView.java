package iris.playharmony.view.playlist;

import iris.playharmony.controller.NavController;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.view.View;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class PlaylistView extends VBox implements View {

    private static int SPACING = 15;

    private Playlist playlist;

    public PlaylistView(Playlist playlist) {
        super(SPACING);
        this.playlist = playlist;
        initElements();
        setPadding(new Insets(SPACING));
    }

    private void initElements() {
        title(playlist.getName());
        for (Song song : playlist.getSongList()) {
            button(song.getTitle(), null);
        }
        button("Add Song", event -> addSong());
    }

    private void addSong() {
        NavController.get().pushView(new AddSongToPlaylistView(playlist));
    }
}
