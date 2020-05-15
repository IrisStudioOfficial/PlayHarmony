package iris.playharmony.view.main;

import iris.playharmony.controller.NavController;
import iris.playharmony.model.Playlist;
import iris.playharmony.session.Session;
import iris.playharmony.util.OnRefresh;
import iris.playharmony.view.playlist.CreatePlaylistView;
import iris.playharmony.view.playlist.PlaylistView;
import iris.playharmony.view.song.UserSongListView;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class UserView extends VBox {

    private ObservableList<Playlist> playlists;

    private TableView playlistsTable;
    private Pagination pagination;

    private static int SPACING = 15;

    public UserView() {
        super(SPACING);
        this.playlists = getPlaylists();
        initElements();
        setPadding(new Insets(SPACING));
    }

    private void initElements() {
        TextFactory.label("Select Playlist", DefaultStyle.title());

        add(playlistsTable = TableFactory.table(playlists,
                TableFactory.tableColumn("Name", "name"),
                TableFactory.tableColumn("Nr of songs", "size")
        ));

        add(pagination = TableFactory.pagination(playlists, playlistsTable));

        add(getBottomButtonPanel());
    }

    private Node add(Node node) {
        getChildren().add(node);
        return node;
    }

    private Node getBottomButtonPanel() {
        Region padding = new Region();
        padding.setPrefWidth(5);
        Region padding2 = new Region();
        padding2.setPrefWidth(5);
        return new HBox(
                ButtonFactory.button("Search Songs", e -> NavController.get().pushView(new UserSongListView())),
                padding,
                ButtonFactory.button("Add Playlist", e -> NavController.get().pushView(new CreatePlaylistView())),
                padding2,
                ButtonFactory.button("See Playlist", event -> seePlaylist())
        );
    }

    private void seePlaylist() {
        Playlist selectedItem = (Playlist) playlistsTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            NavController.get().pushView(new PlaylistView(selectedItem));
        }
    }

    private ObservableList<Playlist> getPlaylists() {
        ObservableList<Playlist> data = FXCollections.observableArrayList();
        data.addAll(Session.getSession().currentUser().getPlayLists());
        return data;
    }

    @OnRefresh
    public void refresh() {
        playlists = getPlaylists();
        TableFactory.updateTable(playlists, playlistsTable);
        TableFactory.updatePagination(playlists, playlistsTable, pagination);
    }
}
