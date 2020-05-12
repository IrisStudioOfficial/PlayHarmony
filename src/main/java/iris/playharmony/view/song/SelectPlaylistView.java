package iris.playharmony.view.song;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.view.View;
import iris.playharmony.view.playlist.PlaylistView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class SelectPlaylistView extends VBox implements View {

    private static int SPACING = 15;

    private Playlist playlist;
    private TableView songsTable;
    private Pagination pagination;

    public SelectPlaylistView() {
        super(SPACING);
        initElements();
        setPadding(new Insets(SPACING));
    }

    private void initElements() {
        title("Select playlist");

        songsTable = table(getPlaylists(),
                tableColumn("Name", "name"),
                tableColumn("Nr of songs", "size")
        );
        pagination = pagination(getPlaylists(), songsTable);

        button("Add to playlist", event -> {
            NavController.get().popView();
        });
    }

    private ObservableList<Playlist> getPlaylists() {
        ObservableList<Playlist> data = FXCollections.observableArrayList();
        Playlist playlist = new Playlist("Playlist 1");
        playlist.addSong(new Song().setTitle("Song 1"));
        playlist.addSong(new Song().setTitle("Song 2"));
        data.add(new Playlist("Playlist 1"));

        playlist = new Playlist("Playlist 2");
        playlist.addSong(new Song().setTitle("Song 1"));
        data.add(playlist);

        playlist = new Playlist("Playlist 3");
        playlist.addSong(new Song().setTitle("Song 1"));
        playlist.addSong(new Song().setTitle("Song 2"));
        playlist.addSong(new Song().setTitle("Song 3"));
        playlist.addSong(new Song().setTitle("Song 4"));
        data.add(playlist);

        return data;
    }
}
