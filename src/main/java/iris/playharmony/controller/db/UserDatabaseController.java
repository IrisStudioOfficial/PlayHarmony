package iris.playharmony.controller.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iris.playharmony.controller.handler.PathHandler;
import iris.playharmony.model.Email;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Role;
import iris.playharmony.model.User;
import iris.playharmony.util.FileUtils;
import iris.playharmony.util.Json;
import iris.playharmony.util.Resources;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDatabaseController extends AbstractDatabaseController implements IUserDatabaseController {

    private static final String SQL_QUERY_SELECT_ALL_USERS = "SELECT * FROM USERS";

    private static final String SQL_QUERY_INSERT_NEW_USER = "INSERT INTO USERS (PHOTO, NAME, SURNAME, CATEGORY, USER_ROLE, EMAIL)" +
            " VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_QUERY_UPDATE_USER = "UPDATE USERS SET" +
            " PHOTO = ?," +
            " NAME = ?," +
            " SURNAME = ?," +
            " CATEGORY = ?," +
            " USER_ROLE = ?," +
            " EMAIL = ? " +
            "WHERE EMAIL = ?";

    public static final String SQL_QUERY_REMOVE_USER_BY_EMAIL = "DELETE FROM USERS" +
            " WHERE email = ?";


    @Override
    public List<User> getUsers() {

        List<User> userList = new ArrayList<>();

        try(Statement statement = getDBConnection().createStatement()) {

            ResultSet resultSet = statement.executeQuery(SQL_QUERY_SELECT_ALL_USERS);

            readUsersFromDatabase(resultSet, userList);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    private void readUsersFromDatabase(ResultSet resultSet, List<User> userList) throws SQLException {

        while(resultSet.next()) {

            File photo = FileUtils.writeToTemporalFile(resultSet.getBinaryStream("photo"));

            List<Playlist> playList = Json.fromJson(List.class, resultSet.getString("PLAYLIST"));

            // List<Playlist> playList = new Gson().fromJson(resultSet.getString("PLAYLIST"), new TypeToken<List<Playlist>>(){}.getType());

            playList = playList == null ? new ArrayList<>() : playList;

            User user = new User()
                    .name(resultSet.getString("NAME"))
                    .surname(resultSet.getString("SURNAME"))
                    .category("CATEGORY")
                    .role(Role.getRoleFrom(resultSet.getString("USER_ROLE")))
                    .mail(new Email(resultSet.getString("EMAIL")))
                    .photo(photo)
                    .setPlayLists(playList);
                    //.setPassword(resultSet.getString("PASSWORD"));

            userList.add(user);
        }
    }

    @Override
    public boolean addUser(User user) {

        checkUser(user);

        if (!userExists(user)) {

            try(PreparedStatement statement = getDBConnection().prepareStatement(SQL_QUERY_INSERT_NEW_USER)) {

                File photo = user.getPhoto();

                FileUtils.readFileBinary(photo, inputStream -> statement.setBinaryStream(1, inputStream, (int)photo.length()));

                DBObjectSerializer serializer = new DBObjectSerializer();

                serializer.serialize(user, statement);

                statement.executeUpdate();

                return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public void checkUser(User user) {
        if(user.getName().isEmpty() || user.getSurname().isEmpty() || user.getCategory().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public boolean userExists(User user) {
        return getUsers().contains(user);
    }

    @Override
    public boolean updateUser(User user, String key) {

        checkUser(user);

        if(userExists(user)) {

            try(PreparedStatement statement = getDBConnection().prepareStatement(SQL_QUERY_UPDATE_USER)) {

                File photo = user.getPhoto();

                final int photoIndex = 1;

                if(photo != null) {

                    FileUtils.readFileBinary(photo, inputStream -> statement.setBinaryStream(photoIndex, inputStream, (int)photo.length()));

                } else {

                    File defaultPhoto = new File(Resources.get(PathHandler.DEFAULT_PHOTO_PATH));

                    FileUtils.readFileBinary(defaultPhoto, inputStream -> statement.setBinaryStream(photoIndex, inputStream, inputStream.available()));
                }

                DBObjectSerializer serializer = new DBObjectSerializer();

                serializer.serialize(user, statement);

                statement.executeUpdate();

                return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public boolean removeUser(String userEmail) {

        if(userEmail == null || userEmail.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if(userExists(userEmail)) {

            try(PreparedStatement statement = getDBConnection().prepareStatement(SQL_QUERY_REMOVE_USER_BY_EMAIL)) {

                statement.setString(1, userEmail);

                statement.executeUpdate();

                return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private boolean userExists(String userEmail) {
        return getUsers().stream().anyMatch(user -> Objects.equals(user.getEmail(), userEmail));
    }

}
