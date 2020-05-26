package iris.playharmony.controller.db;

import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.model.SongReview;
import iris.playharmony.model.User;

import java.util.List;

public interface ISongDatabaseController {

    boolean addSong(Song song);

    boolean deleteSong(Song song);

    List<Song> getSongs();

    List<SongReview> getSongReviews();
}
