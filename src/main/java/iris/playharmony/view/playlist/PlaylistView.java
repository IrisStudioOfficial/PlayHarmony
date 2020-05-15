package iris.playharmony.view.playlist;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.session.Session;
import iris.playharmony.util.OnRefresh;
import iris.playharmony.view.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class PlaylistView extends VBox {

    private Playlist playlist;
    private ObservableList<ObservableSong> songs;

    private TableView songsTable;
    private Pagination pagination;
    private Label playModeLabel;

    private static int SPACING = 15;

    public PlaylistView(Playlist playlist) {
        super(SPACING);
        this.playlist = playlist;
        songs = getSongs();
        initElements();
        setPadding(new Insets(SPACING));
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

    private void initElements() {
        add(TextFactory.label(playlist.getName(), DefaultStyle.label()));

        add(songsTable = TableFactory.table(songs,
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Title", "title"),
                TableFactory.tableColumn("Author", "author"),
                TableFactory.tableColumn("Date", "date")
        ));

        add(pagination = TableFactory.pagination(songs, songsTable));

        add(ButtonFactory.button("Add Song", event -> addSong()));
        add(ButtonFactory.button("Delete Song", event -> deleteSong()));
        add(ButtonFactory.button("Change Song Play Mode", event -> changeSongPlayMode()));

        add(playModeLabel = TextFactory.label("Song Play Mode: " + playlist.getSongPlayMode().toString(), DefaultStyle.label()));
    }

    private void add(Node node) {
        getChildren().add(node);
    }

    private void addSong() {
        NavController.get().pushView(new AddSongToPlaylistView(playlist));
    }

    private void deleteSong() {
        ObservableSong selectedItem = (ObservableSong) songsTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            if(AlertFactory.confirmAlert("Remove Song", "Do you want to delete the song?")) {
                Song sonPrueba = new DatabaseController().getSongs().stream()
                        .filter(song -> song.getTitle().equals(selectedItem.getTitle()))
                        .findAny().get();
                playlist.deleteSong(sonPrueba);
                new DatabaseController().addPlayList(playlist, Session.getSession().currentUser());
                refresh();
            }
        }
    }

    private void changeSongPlayMode() {
        playlist.changeSongPlayMode();
        playModeLabel.setText("Song Play Mode: " + playlist.getSongPlayMode().toString());
    }

    @OnRefresh
    public void refresh() {
        songs = getSongs();
        TableFactory.updateTable(songs, songsTable);
        TableFactory.updatePagination(songs, songsTable, pagination);
    }
}
