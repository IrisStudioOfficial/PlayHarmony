package iris.playharmony.controller.db;

import iris.playharmony.controller.db.sql.SQLStatement;
import iris.playharmony.controller.db.sql.SQLUpdateQuery;
import iris.playharmony.controller.db.sql.SQLWriteQuery;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.User;
import iris.playharmony.util.Json;

public class PlaylistDatabaseController extends AbstractDatabaseController implements IPlaylistDatabaseController {

    private static final String USER_TABLE_NAME = "USERS";

    private static final SQLWriteQuery SQL_QUERY_UPDATE_PLAYLIST = new SQLUpdateQuery(USER_TABLE_NAME,
            "email",
            "playlist");

    private static final SQLWriteQuery SQL_QUERY_UPDATE_FAVOURITES = new SQLUpdateQuery(USER_TABLE_NAME,
            "email",
            "favourites");


    public PlaylistDatabaseController() {
    }

    @Override
    public boolean addPlayList(Playlist playlist, User user) {

        removeUserPlaylist(playlist, user);

        user.addPlayList(playlist);

        return updateUserPlaylists(SQL_QUERY_UPDATE_PLAYLIST, "playlist", Json.toJson(user.getPlayLists()), user);
    }

    @Override
    public boolean updatePlayList(String newName, Playlist playlist, User user){
        removeUserPlaylist(playlist, user);

        playlist.setName(newName);

        user.addPlayList(playlist);

        return updateUserPlaylists(SQL_QUERY_UPDATE_PLAYLIST, "playlist", Json.toJson(user.getPlayLists()), user);
    }

    @Override
    public boolean deletePlayList(Playlist playList, User user) {

        removeUserPlaylist(playList, user);

        return updateUserPlaylists(SQL_QUERY_UPDATE_PLAYLIST, "playlist", Json.toJson(user.getPlayLists()), user);
    }
    

    @Override
    public boolean addToFavourites(Playlist favourites, User user) {

        user.favourites(favourites);

        return updateUserPlaylists(SQL_QUERY_UPDATE_FAVOURITES, "favourites", Json.toJson(favourites), user);
    }

    private void removeUserPlaylist(Playlist playlist, User user) {
        user.getPlayLists().removeIf(pl -> pl.getName().equals(playlist.getName()));
    }

    private boolean updateUserPlaylists(SQLWriteQuery sql, String paramName, String json, User user) {

        try(SQLStatement statement = sql.prepareStatement(getDBConnection())) {

            statement.setKey("email", user.getEmail().toString())
                    .set(paramName, json);

            return statement.execute() != SQLStatement.ERROR_CODE;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
