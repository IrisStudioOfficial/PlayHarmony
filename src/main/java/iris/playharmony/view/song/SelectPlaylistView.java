package iris.playharmony.view.song;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.Playlist;
import iris.playharmony.session.Session;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TableFactory;
import iris.playharmony.view.util.TextFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class SelectPlaylistView extends VBox {

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
        TextFactory.label("Select Playlist", DefaultStyle.title());

        add(playlistsTable = TableFactory.table(getPlaylists(),
                TableFactory.tableColumn("Name", "name"),
                TableFactory.tableColumn("Nr of songs", "size")
        ));

        add(pagination = TableFactory.pagination(getPlaylists(), playlistsTable));

        add(ButtonFactory.button("Add To Playlist", event -> addToPlaylist()));
    }

    private Node add(Node node) {
        getChildren().add(node);
        return node;
    }

    private void addToPlaylist() {
        Playlist selectedPlaylist = (Playlist) playlistsTable.getSelectionModel().getSelectedItem();
        selectedPlaylist.addSong(new DatabaseController().getSongs().stream()
                .filter(song -> song.getTitle().equals(toBeAddedSong))
                .findAny().get());
        new DatabaseController().addPlayList(selectedPlaylist, Session.getSession().currentUser());

        NavController.get().popView();
    }

    private ObservableList<Playlist> getPlaylists() {
        ObservableList<Playlist> data = FXCollections.observableArrayList();
        data.addAll(Session.getSession().currentUser().getPlayLists());
        return data;
    }
}
