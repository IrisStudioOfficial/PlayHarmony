package iris.playharmony.view.song;

import com.google.gson.Gson;
import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.controller.handler.PathHandler;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.model.User;
import iris.playharmony.session.Session;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TableFactory;
import iris.playharmony.view.util.TextFactory;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserSongListView extends SongListView {

    public UserSongListView() {
        super();
    }

    @Override
    public void initElements() {
        add(TextFactory.label("UserSongListView", DefaultStyle.title()));
        add(searchForm());
        add(songsTable = TableFactory.table(data,
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Title", "title"),
                TableFactory.tableColumn("Author", "author"),
                TableFactory.tableColumn("Date", "date")
        ));

        add(pagination = TableFactory.pagination(data, songsTable));
        add(ButtonFactory.button("Add To Playlist", this::selectPlaylist));
        add(ButtonFactory.button("Add To Favourites", this::addToFavourites));
        add(ButtonFactory.button("Play song", this::playSong));
    }

    private void addToFavourites(ActionEvent actionEvent) {
        Playlist favourites = Session.getSession().currentUser().favourites();
        favourites = favourites == null ? new Playlist("Favourites") : favourites;

        ObservableSong selectedItem = (ObservableSong) songsTable.getSelectionModel().getSelectedItem();
        Song toBeAdded = new DatabaseController().getSongs().stream()
                .filter(song -> song.getTitle().equals(selectedItem.getTitle()))
                .findAny().get();

        favourites.addSong(toBeAdded);
        addFavourites(favourites, Session.getSession().currentUser());
    }

    protected void selectPlaylist(ActionEvent event) {
        ObservableSong selectedItem = (ObservableSong) songsTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
            NavController.get().pushView(new SelectPlaylistView(selectedItem.getTitle()));
    }

    public boolean addFavourites(Playlist favourites, User user) {
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


