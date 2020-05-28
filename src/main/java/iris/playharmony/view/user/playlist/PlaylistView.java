package iris.playharmony.view.user.playlist;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.session.Session;
import iris.playharmony.view.template.ListTemplate;
import iris.playharmony.view.user.MusicPlayerController;
import iris.playharmony.view.user.song.AddSongToPlaylistView;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;

import java.util.Comparator;

public class PlaylistView extends ListTemplate<ObservableSong> {

    private Playlist playlist;
    MusicPlayerController musicPlayerController;
    int index = 0;

    public PlaylistView(Playlist playlist) {
        super(playlist.getName(), playlist);
        musicPlayerController = new MusicPlayerController(playlist);
    }

    @Override
    protected void initBaseElement(Object playlist) {
        this.playlist = (Playlist) playlist;
    }

    @Override
    protected Comparator<ObservableSong> initComparator() {
        return Comparator.comparing(o -> o.title().get());
    }

    @Override
    protected ObservableList<ObservableSong> getData() {
        ObservableList<ObservableSong> data = FXCollections.observableArrayList();
        playlist.getSongList().stream()
                .map(ObservableSong::from)
                .forEach(data::add);
        return data;
    }

    @Override
    protected String fieldToFilter(ObservableSong song) {
        return song.getTitle();
    }

    @Override
    protected TableColumn[] initTable() {
        return new TableColumn[] {
                TableFactory.tableColumnPhoto("", "fav", 40),
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Title", "title"),
                TableFactory.tableColumn("Author", "author"),
                TableFactory.tableColumn("Date", "date"),
                TableFactory.tableColumn("Avg. Rating", "rating")
        };
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Add Song", event -> addSong()),
                ButtonFactory.button("Delete Song", event -> deleteSong()),
                ButtonFactory.button("Play Song", event -> musicPlayerController.playSong(getSelectedItem()))
        };
    }

    private void addSong() {
        NavController.get().pushView(new AddSongToPlaylistView(playlist));
    }

    private void deleteSong() {
        ObservableSong selectedItem = getSelectedItem();
        if(selectedItem != null) {
            if(AlertFactory.confirmAlert("Remove Song", "Do you want to delete the song?")) {
                Song songPrueba = DatabaseController.get().getSongs().stream()
                        .filter(song -> song.getTitle().equals(selectedItem.getTitle()))
                        .findAny().get();
                playlist.deleteSong(songPrueba);
                DatabaseController.get().addPlayList(playlist, Session.getSession().currentUser());
                refresh();
            }
        }
    }
}