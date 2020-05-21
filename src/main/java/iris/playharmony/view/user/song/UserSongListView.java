package iris.playharmony.view.user.song;

import com.google.gson.Gson;
import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.controller.handler.PathHandler;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.model.User;
import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.model.player.Spectrum;
import iris.playharmony.session.Session;
import iris.playharmony.util.OnRefresh;
import iris.playharmony.view.player.MusicPlayerView;
import iris.playharmony.view.player.MusicPlayerViewModel;
import iris.playharmony.view.template.ListTemplate;
import iris.playharmony.view.user.playlist.SelectPlaylistView;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.animation.Interpolator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Comparator;

public class UserSongListView extends ListTemplate {

    private TextField searchField;
    private ObservableList<ObservableSong> data;
    private Comparator<ObservableSong> comparator;

    public UserSongListView() {
        super("Search Song");
    }

    @Override
    protected void initElements() {
        comparator = Comparator.comparing(observable -> observable.title().get());
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
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Title", "title"),
                TableFactory.tableColumn("Author", "author"),
                TableFactory.tableColumn("Date", "date")
        ));
    }

    @Override
    protected void initPagination() {
        add(pagination = TableFactory.pagination(data, table));
    }

    @Override
    protected ObservableList<ObservableSong> getObservableData() {
        data = FXCollections.observableArrayList();
        new DatabaseController()
                .getSongs()
                .stream()
                .map(ObservableSong::from)
                .sorted(comparator)
                .forEach(data::add);
        return data;
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Add To Playlist", this::selectPlaylist),
                ButtonFactory.button("Add To Favourites", this::addToFavourites),
                ButtonFactory.button("Play song", this::playSong)
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
        data = data.filtered(observableSong -> observableSong.getTitle().toLowerCase().contains(searchField.getText().toLowerCase()));
        TableFactory.updateTable(data, table);
        TableFactory.updatePagination(data, table, pagination);
    }

    private void selectPlaylist(ActionEvent event) {
        ObservableSong selectedItem = (ObservableSong) table.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
            NavController.get().pushView(new SelectPlaylistView(selectedItem.getTitle()));
    }

    private void playSong(ActionEvent actionEvent) {
        MusicPlayer musicPlayer = new MusicPlayer();
        Spectrum spectrum = new Spectrum(Interpolator.LINEAR);
        ObservableSong selectedItem = (ObservableSong) table.getSelectionModel().getSelectedItem();
        Song song = new DatabaseController().getSongs().stream().filter(s -> s.getTitle().equals(selectedItem.getTitle())).findFirst().get();

        MusicPlayerViewModel musicPlayerViewModel = new MusicPlayerViewModel(musicPlayer, spectrum);
        musicPlayerViewModel.setSong(song);

        NavController.get().pushView(new MusicPlayerView(musicPlayerViewModel));
        musicPlayer.play();
    }

    private void addToFavourites(ActionEvent actionEvent) {
        Playlist favourites = Session.getSession().currentUser().favourites();
        favourites = favourites == null ? new Playlist("Favourites") : favourites;

        ObservableSong selectedItem = (ObservableSong) table.getSelectionModel().getSelectedItem();
        Song toBeAdded = new DatabaseController().getSongs().stream()
                .filter(song -> song.getTitle().equals(selectedItem.getTitle()))
                .findAny().get();

        favourites.addSong(toBeAdded);
        addFavourites(favourites, Session.getSession().currentUser());
    }

    private boolean addFavourites(Playlist favourites, User user) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + PathHandler.DATABASE_PATH;
            connection = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.favourites(favourites);

        String sql = "UPDATE USERS SET FAVOURITES = ? WHERE EMAIL = ?";
        try(PreparedStatement pst = connection.prepareStatement(sql)){
            pst.setString(1, new Gson().toJson(user.favourites()));
            pst.setString(2, user.getEmail().toString());
            return pst.executeUpdate() == 1;
        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }
}


