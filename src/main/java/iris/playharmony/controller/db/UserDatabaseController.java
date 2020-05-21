package iris.playharmony.controller.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iris.playharmony.controller.handler.PathHandler;
import iris.playharmony.exceptions.RemoveUserException;
import iris.playharmony.exceptions.UpdateUserException;
import iris.playharmony.model.Email;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Role;
import iris.playharmony.model.User;
import iris.playharmony.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDatabaseController extends AbstractDatabaseController implements IUserDatabaseController {

    public static final String SQL_QUERY_SELECT_ALL_USERS = "SELECT * FROM USERS";

    public static final String SQL_QUERY_INSERT_NEW_USER = "INSERT INTO USERS (PHOTO, NAME, SURNAME, CATEGORY, USER_ROLE, EMAIL) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String SQL_QUERY_UPDATE_USER = "UPDATE USERS SET PHOTO = ?, NAME = ?, SURNAME = ?, CATEGORY = ?, USER_ROLE = ?, EMAIL = ? " +
            "WHERE EMAIL = ?";


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

            List<Playlist> playList = new Gson().fromJson(resultSet.getString("PLAYLIST"), new TypeToken<List<Playlist>>(){}.getType());

            playList = playList == null ? new ArrayList<>() : playList;

            User user = new User()
                    .name(resultSet.getString("NAME"))
                    .surname(resultSet.getString("SURNAME"))
                    .category("CATEGORY")
                    .role(Role.getRoleFrom("USER_ROLE"))
                    .mail(new Email(resultSet.getString("EMAIL")))
                    .photo(photo)
                    .setPlayLists(playList)
                    .setPassword(resultSet.getString("PASSWORD"));

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

                statement.setString(2, user.getName());
                statement.setString(3, user.getSurname());
                statement.setString(4, user.getCategory());
                statement.setString(5, user.getRole().toString());
                statement.setString(6, user.getEmail().toString());
                statement.setString(8, user.getPassword());

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

                if(photo != null) {

                    FileUtils.readFileBinary(photo, inputStream -> statement.setBinaryStream(1, inputStream, (int)photo.length()));

                } else {

                    try (InputStream fis = getClass().getResourceAsStream("/" + PathHandler.DEFAULT_PHOTO_PATH)) {
                        ps.setBinaryStream(1, fis, fis.available());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                ps.setString(2, user.getName());
                ps.setString(3, user.getSurname());
                ps.setString(4, user.getCategory());
                ps.setString(5, user.getRole().toString());
                ps.setString(6, user.getEmail().toString());
                ps.setString(7, key);

                ps.executeUpdate();

                close(ps);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean removeUser(String key) throws RemoveUserException {
        if(key == null || key.equals(""))
            throw new RemoveUserException();
        if (getUsers().stream().anyMatch(databaseUser -> databaseUser.getEmail().toString().equals(key))) {
            try {
                String sql = "DELETE FROM USERS WHERE email = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, key);

                ps.executeUpdate();

                close(ps);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
