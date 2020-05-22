package iris.playharmony.controller.db;

import iris.playharmony.model.Song;
import iris.playharmony.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SongDatabaseController extends AbstractDatabaseController implements ISongDatabaseController {

    private static final String SQL_QUERY_INSERT_NEW_SONG = "INSERT INTO SONGS (title, author, photo, publication, pathFile)" +
            " VALUES(?,?,?,?,?)";

    public static final String SQL_QUERY_GET_ALL_SONGS = "SELECT * FROM SONGS";
    public static final String SQL_QUERY_DELETE_SONG_BY_TITLE = "DELETE FROM SONGS WHERE title = ?";


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

    private void readSongsDatabase(ResultSet resultSet, List<Song> songList) throws SQLException {

        while(resultSet.next()) {

            FileUtils.writeToTemporalFile(resultSet.getBinaryStream("photo"));

            Song song = new Song()
                    .setTitle(resultSet.getString("TITLE"))
                    .setDate(resultSet.getString("PUBLICATION"))
                    .setPathFile(resultSet.getString("PATHFILE"))
                    .setAuthor(resultSet.getString("AUTHOR"))
                    .setPhoto(resultSet.toString());

            songList.add(song);
        }
    }

    @Override
    public boolean addSong(Song song){

        try(PreparedStatement statement = getDBConnection().prepareStatement(SQL_QUERY_INSERT_NEW_SONG)) {

            File songPhoto = new File(song.getPhoto());

            FileUtils.readFileBinary(songPhoto, inputStream -> statement.setBinaryStream(3, inputStream, (int)songPhoto.length()));

            DBObjectSerializer serializer = new DBObjectSerializer();

            serializer.serialize(song, statement);

            return statement.executeUpdate() == 1;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteSong(Song song) {

        try(PreparedStatement statement = getDBConnection().prepareStatement(SQL_QUERY_DELETE_SONG_BY_TITLE)) {

            statement.setString(1, song.getTitle());

            return statement.executeUpdate() == 1;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

}
