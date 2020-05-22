package iris.playharmony.view.user;

import iris.playharmony.controller.NavController;
import iris.playharmony.model.Playlist;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class UserView extends ListTemplate {

    private TextField searchField;
    private ObservableList<Playlist> data;

    public UserView() {
        super("Select Playlist");
    }

    @Override
    protected void initElements() {
        data = getObservableData();
        searchField = new TextField();
    }

    @Override
    protected void initSearchForm() {
        HBox searchRow = new HBox();
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        Region padding = new Region();
        padding.setPrefWidth(5);
        searchField.setOnAction(event -> searchCommand());
        searchRow.getChildren().add(region);
        searchRow.getChildren().add(searchField);
        searchRow.getChildren().add(ButtonFactory.button("Search", event -> searchCommand()));

        add(searchRow);
    }

    @Override
    protected void initTable() {
        add(table = TableFactory.table(data,
                TableFactory.tableColumn("Name", "name"),
                TableFactory.tableColumn("Nr of songs", "size")
        ));
    }

    @Override
    protected void initPagination() {
        add(pagination = TableFactory.pagination(data, table));
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

    private void searchCommand() {
        refresh();

        if(searchField.getText().isEmpty())
            return;
        data = data.filtered(playlist -> playlist.getName().toLowerCase().contains(searchField.getText().toLowerCase()));
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
