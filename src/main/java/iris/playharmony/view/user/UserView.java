package iris.playharmony.view.user;

import iris.playharmony.controller.NavController;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.User;
import iris.playharmony.session.Session;
import iris.playharmony.util.OnRefresh;
import iris.playharmony.view.template.ListTemplate;
import iris.playharmony.view.user.playlist.CreatePlaylistView;
import iris.playharmony.view.user.playlist.PlaylistView;
import iris.playharmony.view.user.song.UserSongListView;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;

import java.util.Comparator;

public class UserView extends ListTemplate<Playlist> {

    public UserView() {
        super("Select Playlist");
        init();
    }

    @Override
    protected void initElements() {

    }

    @Override
    protected void searchCommand() {
        if(!searchField.getText().isEmpty()) {
            data = data.filtered(playlist -> playlist.getName().toLowerCase().contains(searchField.getText().toLowerCase()));
            TableFactory.updateTable(data, table);
            TableFactory.updatePagination(data, table, pagination);
        }
    }

    @Override
    protected TableView initTable() {
        return TableFactory.table(data,
                TableFactory.tableColumn("Name", "name"),
                TableFactory.tableColumn("Nr of songs", "size")
        );
    }

    @Override
    protected Pagination initPagination() {
        return TableFactory.pagination(data, table);
    }

    @Override
    protected Comparator<Playlist> getComparator() {
        return Comparator.comparing(Playlist::getName);
    }

    @Override
    protected ObservableList<Playlist> getObservableData() {
        ObservableList<Playlist> data = FXCollections.observableArrayList();
        User user = Session.getSession().currentUser();
        if(user.getPlayLists() == null) {
            return FXCollections.emptyObservableList();
        }
        user.getPlayLists().stream()
                .sorted(comparator)
                .forEach(data::add);
        return data;
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Search Songs", e -> NavController.get().pushView(new UserSongListView())),
                ButtonFactory.button("Add Playlist", e -> NavController.get().pushView(new CreatePlaylistView())),
                ButtonFactory.button("See Playlist", event -> seePlaylist())
        };
    }

    @OnRefresh
    @Override
    public void refresh() {
        data = getObservableData();
        TableFactory.updateTable(data, table);
        TableFactory.updatePagination(data, table, pagination);
    }

    private void seePlaylist() {
        Playlist selectedItem = (Playlist) table.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            NavController.get().pushView(new PlaylistView(selectedItem));
        }
    }
}
