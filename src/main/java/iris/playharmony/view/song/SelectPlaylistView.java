package iris.playharmony.view.song;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.Playlist;
import iris.playharmony.session.Session;
import iris.playharmony.view.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class SelectPlaylistView extends VBox implements View {

    private static int SPACING = 15;

    private Playlist playlist;
    private TableView playlistsTable;
    private Pagination pagination;
    private String toBeAddedSong;

    public SelectPlaylistView(String songTitle) {

        super(SPACING);
        initElements();
        setPadding(new Insets(SPACING));
        this.toBeAddedSong = songTitle;
    }

    private void initElements() {

        title("Select playlist");

        playlistsTable = table(getPlaylists(),
                tableColumn("Name", "name"),
                tableColumn("Nr of songs", "size")
        );

        pagination = pagination(getPlaylists(), playlistsTable);

        button("Add to playlist", event -> {
            Playlist selectedPlaylist = (Playlist)playlistsTable.getSelectionModel().getSelectedItem();
            selectedPlaylist.addSong(new DatabaseController().getSongs().stream()
                    .filter(song -> song.getTitle().equals(toBeAddedSong))
                    .findAny().get());
            new DatabaseController().addPlayList(selectedPlaylist, Session.getSession().currentUser());

            NavController.get().popView();

        });
    }

    private ObservableList<Playlist> getPlaylists() {

        ObservableList<Playlist> data = FXCollections.observableArrayList();
        data.addAll(Session.getSession().currentUser().getPlayLists());
        return data;
    }
}
