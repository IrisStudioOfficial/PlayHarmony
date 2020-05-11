package iris.playharmony.view.playlist;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.view.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class AddSongToPlaylistView extends VBox implements View {

    private static int SPACING = 15;

    private Playlist playlist;
    private TableView songsTable;
    private Pagination pagination;

    public AddSongToPlaylistView(Playlist playlist) {
        super(SPACING);
        this.playlist = playlist;
        initElements();
        setPadding(new Insets(SPACING));
    }

    private void initElements() {
        title("Add Song to Playlist");

        songsTable = table(getSongs(),
                tableColumnPhoto("Photo", "photo"),
                tableColumn("Title", "title"),
                tableColumn("Author", "author"),
                tableColumn("Date", "date")
        );
        pagination = pagination(getSongs(), songsTable);

        button("Add Song", event -> addSongs());
        button("Return", event -> {
            NavController.get().popView();
            PlaylistView playlistView = NavController.get().getCurrentView();
            playlistView.update();
        });
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
            List<Song> songs = new DatabaseController().getSongs().stream()
                    .filter(s -> s.getTitle().equals(selectedItem.getTitle()))
                    .collect(Collectors.toList());
            if(songs.size() > 0) {
                playlist.addSong(songs.get(0));
                updateTable(getSongs(), songsTable);
                updatePagination(getSongs(), songsTable, pagination);
            }
        }
    }
}
