package iris.playharmony.controller.db;

import iris.playharmony.controller.db.sql.SQLDeleteByKeyQuery;
import iris.playharmony.controller.db.sql.SQLInsertQuery;
import iris.playharmony.controller.db.sql.SQLStatement;
import iris.playharmony.controller.db.sql.SQLWriteQuery;
import iris.playharmony.model.Song;
import iris.playharmony.model.SongReview;
import iris.playharmony.util.FileUtils;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SongDatabaseController extends AbstractDatabaseController implements ISongDatabaseController {

    private static final String SONGS_TABLE_NAME = "SONGS";

    private static final String SQL_QUERY_GET_ALL_SONGS = "SELECT * FROM SONGS";

    private static final String SONG_REVIEWS_TABLE_NAME = "SONG_REVIEWS";
    private static final SQLWriteQuery SQL_QUERY_INSERT_NEW_SONG = new SQLInsertQuery(SONGS_TABLE_NAME,
            "title", "author", "photo", "publication", "pathFile");

    private static final SQLWriteQuery SQL_QUERY_DELETE_SONG_BY_TITLE = new SQLDeleteByKeyQuery(SONGS_TABLE_NAME, "title");

    private static final SQLWriteQuery SQL_QUERY_INSERT_SONG_RATING = new SQLInsertQuery(SONG_REVIEWS_TABLE_NAME, "user", "song_title", "rating");

    private static final String SQL_QUERY_GET_ALL_REVIEWS = "SELECT * FROM " + SONG_REVIEWS_TABLE_NAME;

    public SongDatabaseController() {
    }

    @Override
    public List<Song> getSongs() {

        List<Song> songList = new ArrayList<>();

        try(Statement statement = getDBConnection().createStatement()) {

            ResultSet resultSet = statement.executeQuery(SQL_QUERY_GET_ALL_SONGS);

            readSongsDatabase(resultSet, songList);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return songList;
    }

    public List<SongReview> getSongReviews() {

        try(Statement statement = getDBConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_QUERY_GET_ALL_REVIEWS);

            return readSongReviewsDatabase(resultSet);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private void readSongsDatabase(ResultSet resultSet, List<Song> songList) throws SQLException {

        while(resultSet.next()) {

            File photo = FileUtils.writeToTemporalFile(resultSet.getBinaryStream("photo"));

            Song song = new Song()
                    .setTitle(resultSet.getString("TITLE"))
                    .setAuthor(resultSet.getString("AUTHOR"))
                    .setPhoto(photo.getAbsolutePath())
                    .setDate(resultSet.getString("PUBLICATION"))
                    .setPathFile(resultSet.getString("PATHFILE"));

            songList.add(song);
        }
    }

    private List<SongReview> readSongReviewsDatabase(ResultSet resultSet) throws SQLException {
        List<SongReview> songReviews = new ArrayList<>();

        while(resultSet.next()) {
            songReviews.add(new SongReview()
                    .setId(resultSet.getInt("PK"))
                    .setSongTitle(resultSet.getString("SONG_TITLE"))
                    .setUser(resultSet.getString("USER"))
                    .setRating(resultSet.getInt("RATING")));
        }

        return songReviews;
    }

    @Override
    public boolean addSong(Song song) {

        try(SQLStatement statement = SQL_QUERY_INSERT_NEW_SONG.prepareStatement(getDBConnection())) {

            File songPhoto = new File(song.getPhoto());

            FileUtils.readFileBinary(songPhoto, inputStream -> statement.set("photo", inputStream, (int)songPhoto.length()));

            statement.set("title", song.getTitle())
                    .set("author", song.getAuthor())
                    .set("publication", song.getDate())
                    .set("pathFile", song.getPathFile());

            return statement.execute() != SQLStatement.ERROR_CODE;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteSong(Song song) {

        try(SQLStatement statement = SQL_QUERY_DELETE_SONG_BY_TITLE.prepareStatement(getDBConnection())) {

            statement.setKey("title", song.getTitle());

            return statement.execute() != SQLStatement.ERROR_CODE;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
