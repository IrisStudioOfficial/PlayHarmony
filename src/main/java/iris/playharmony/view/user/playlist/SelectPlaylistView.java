package iris.playharmony.view.user.playlist;

import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.Playlist;
import iris.playharmony.session.Session;
import iris.playharmony.view.template.ListTemplate;
import iris.playharmony.view.user.song.UserSongListView;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;

import java.util.Comparator;

public class SelectPlaylistView extends ListTemplate<Playlist> {

    private String toBeAddedSong;

    public SelectPlaylistView(Object toBeAddedSong) {
        super("Select Playlist", toBeAddedSong);
    }

    @Override
    protected void initBaseElement(Object toBeAddedSong) {
        this.toBeAddedSong = (String) toBeAddedSong;
    }

    @Override
    protected Comparator<Playlist> initComparator() {
        return Comparator.comparing(Playlist::getName);
    }

    @Override
    protected ObservableList<Playlist> getData() {
        ObservableList<Playlist> playlists = FXCollections.observableArrayList();
        playlists.addAll(Session.getSession().currentUser().getPlayLists());
        return playlists;
    }

    @Override
    protected String fieldToFilter(Playlist playlist) {
        return playlist.getName();
    }

    @Override
    protected TableColumn[] initTable() {
        return new TableColumn[] {
                TableFactory.tableColumn("Name", "name"),
                TableFactory.tableColumn("Nr of songs", "size")
        };
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Add To Playlist", event -> addToPlaylist())
        };
    }

    private void addToPlaylist() {

        Playlist selectedPlaylist = getSelectedItem();

        selectedPlaylist.addSong(DatabaseController.get().getSongs().stream()
                .filter(song -> song.getTitle().equals(toBeAddedSong))
                .findAny().get());

        DatabaseController.get().addPlayList(selectedPlaylist, Session.getSession().currentUser());

        NavController.get().popView();
        UserSongListView userSongListView = NavController.get().getCurrentView();
        userSongListView.refresh();
    }
}