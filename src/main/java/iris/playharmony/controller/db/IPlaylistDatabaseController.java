package iris.playharmony.controller.db;

import iris.playharmony.model.Playlist;
import iris.playharmony.model.User;

public interface IPlaylistDatabaseController {

    boolean addPlayList(Playlist updatedPlaylist, User user);

    boolean updatePlayList(String newName, Playlist playlist, User user);

    boolean deletePlayList(Playlist playList, User user);

    boolean addToFavourites(Playlist favourites, User user);
}
