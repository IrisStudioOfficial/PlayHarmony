package iris.playharmony.controller.db;

import iris.playharmony.model.Playlist;
import iris.playharmony.model.User;

public interface IPlaylistDatabaseController {

    boolean addPlayList(Playlist updatedPlaylist, User user);
}
