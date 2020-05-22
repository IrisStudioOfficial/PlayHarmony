package iris.playharmony.view.user.playlist;

import iris.playharmony.controller.DatabaseController;
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
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;

import java.util.Comparator;

public class SelectPlaylistView extends ListTemplate<Playlist> {

    private String toBeAddedSong;

    public SelectPlaylistView(String songTitle) {
        super("Select Playlist");
        this.toBeAddedSong = songTitle;
        init();
    }

    @Override
    protected void initElements() {

    }

    @Override
    protected void searchCommand() {
        if(searchField.getText().isEmpty())
            return;
        data = data.filtered(playlist -> playlist.getName().toLowerCase().contains(searchField.getText().toLowerCase()));
        TableFactory.updateTable(data, table);
        TableFactory.updatePagination(data, table, pagination);
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
        data.addAll(Session.getSession().currentUser().getPlayLists());
        return data;
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Add To Playlist", event -> addToPlaylist())
        };
    }

    @Override
    public void refresh() {
        data = getObservableData();
        TableFactory.updateTable(data, table);
        TableFactory.updatePagination(data, table, pagination);
    }

    private void addToPlaylist() {
        Playlist selectedPlaylist = (Playlist) table.getSelectionModel().getSelectedItem();
        selectedPlaylist.addSong(new DatabaseController().getSongs().stream()
                .filter(song -> song.getTitle().equals(toBeAddedSong))
                .findAny().get());
        new DatabaseController().addPlayList(selectedPlaylist, Session.getSession().currentUser());

        NavController.get().popView();
        UserSongListView userSongListView = NavController.get().getCurrentView();
        userSongListView.refresh();
    }
}