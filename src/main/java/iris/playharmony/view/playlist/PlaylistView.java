package iris.playharmony.view.playlist;

import iris.playharmony.controller.NavController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.view.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class PlaylistView extends VBox implements View {

    private Playlist playlist;
    private TableView songsTable;
    private Pagination pagination;
    private Label label;

    public PlaylistView(Playlist playlist) {
        super(SPACING);
        this.playlist = playlist;
        initElements();
        setPadding(new Insets(SPACING));
    }

    private void initElements() {
        title(playlist.getName());

        songsTable = table(getSongs(),
                tableColumnPhoto("Photo", "photo"),
                tableColumn("Title", "title"),
                tableColumn("Author", "author"),
                tableColumn("Date", "date")
        );
        pagination = pagination(getSongs(), songsTable);

        button("Add Song", event -> addSong());
        button("Change Song Play Mode", event -> changeSongPlayMode());
        label = label("Song Play Mode: " + playlist.getSongPlayMode().toString());
    }

    private ObservableList<ObservableSong> getSongs() {
        ObservableList<ObservableSong> data = FXCollections.observableArrayList();
        for (Song song : playlist.getSongList()) {
            ObservableSong observableSong = new ObservableSong().title(song.getTitle()).author(song.getAuthor())
                    .photo(song.getPhoto()).date(song.getDate()).path(song.getPathFile());
            data.add(observableSong);
        }
        return data;
    }

    private void addSong() {
        NavController.get().pushView(new AddSongToPlaylistView(playlist));
    }

    private void changeSongPlayMode() {
        playlist.changeSongPlayMode();
        label.setText("Song Play Mode: " + playlist.getSongPlayMode().toString());
    }

    public void update() {
        System.out.println("Hola mundo");
        updateTable(getSongs(), songsTable);
        updatePagination(getSongs(), songsTable, pagination);
    }
}
