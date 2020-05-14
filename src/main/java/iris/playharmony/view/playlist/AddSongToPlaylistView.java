package iris.playharmony.view.playlist;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.view.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class AddSongToPlaylistView extends VBox {

    private Playlist playlist;
    private ObservableList<ObservableSong> songs;

    private TableView songsTable;
    private Pagination pagination;

    private static int SPACING = 15;

    public AddSongToPlaylistView(Playlist playlist) {
        super(SPACING);
        this.playlist = playlist;
        songs = getSongs();
        initElements();
        setPadding(new Insets(SPACING));
    }

    private void initElements() {
        add(TextFactory.label("Add Song to Playlist", DefaultStyle.label()));

        add(songsTable = TableFactory.table(songs,
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Title", "title"),
                TableFactory.tableColumn("Author", "author"),
                TableFactory.tableColumn("Date", "date")
        ));
        add(pagination = TableFactory.pagination(songs, songsTable));

        add(ButtonFactory.button("Add Song", event -> addSongs()));
    }

    private void add(Node node) {
        getChildren().add(node);
    }

    private ObservableList<ObservableSong> getSongs() {
        ObservableList<ObservableSong> data = FXCollections.observableArrayList();
        for (Song song : new DatabaseController().getSongs()) {
            ObservableSong observableSong = new ObservableSong().title(song.getTitle()).author(song.getAuthor())
                    .photo(song.getPhoto()).date(song.getDate()).path(song.getPathFile());
            if(playlist.getSongList().stream().noneMatch(s -> s.getTitle().equals(observableSong.getTitle()))) {
                data.add(observableSong);
            }
        }
        return data;
    }

    private void addSongs() {
        ObservableSong selectedItem = (ObservableSong) songsTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            for (Song song : new DatabaseController().getSongs()) {
                if(song.getTitle().equals(selectedItem.getTitle())) {
                    playlist.addSong(song);
                    songs.remove(selectedItem);
                    TableFactory.updateTable(songs, songsTable);
                    TableFactory.updatePagination(songs, songsTable, pagination);
                }
            }
        }
    }
}
