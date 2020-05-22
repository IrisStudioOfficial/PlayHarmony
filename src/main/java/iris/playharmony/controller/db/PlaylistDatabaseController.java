package iris.playharmony.controller.db;

import com.google.gson.Gson;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.User;
import iris.playharmony.util.Json;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlaylistDatabaseController extends AbstractDatabaseController implements IPlaylistDatabaseController {

    public static final String SQL_QUERY_SET_PLAYLIST_TO_USER = "UPDATE USERS SET PLAYLIST = ? WHERE EMAIL = ?";

    @Override
    public boolean addPlayList(Playlist updatedPlaylist, User user) {

        user.getPlayLists().removeIf(playlist -> playlist.getName().equals(updatedPlaylist.getName()));

        user.addPlayList(updatedPlaylist);

        String jsonOfPlayList = Json.toJson(updatedPlaylist);

        try(PreparedStatement pst = getDBConnection().prepareStatement(SQL_QUERY_SET_PLAYLIST_TO_USER)) {

            pst.setString(1, jsonOfPlayList);

            pst.setString(2, user.getEmail().toString());

            return pst.executeUpdate() == 1;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }
}
