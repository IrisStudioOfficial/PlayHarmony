package iris.playharmony.controller.db;

import iris.playharmony.controller.db.sql.SQLStatement;
import iris.playharmony.controller.db.sql.SQLUpdateQuery;
import iris.playharmony.controller.db.sql.SQLWriteQuery;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.User;
import iris.playharmony.util.Json;

public class PlaylistDatabaseController extends AbstractDatabaseController implements IPlaylistDatabaseController {

    private static final String PLAYLIST_TABLE_NAME = "PLAYLIST";

    private static final SQLWriteQuery SQL_QUERY_SET_PLAYLIST_TO_USER = new SQLUpdateQuery(PLAYLIST_TABLE_NAME,
            "email",
            "playlist");


    public PlaylistDatabaseController() {
    }

    @Override
    public boolean addPlayList(Playlist updatedPlaylist, User user) {

        user.getPlayLists().removeIf(playlist -> playlist.getName().equals(updatedPlaylist.getName()));

        user.addPlayList(updatedPlaylist);

        String jsonOfPlayList = Json.toJson(updatedPlaylist);

        try(SQLStatement statement = SQL_QUERY_SET_PLAYLIST_TO_USER.prepareStatement(getDBConnection())) {

            statement.setKey("email", user.getEmail().toString())
                    .set("playlist", jsonOfPlayList);

            return statement.execute() != SQLStatement.ERROR_CODE;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
