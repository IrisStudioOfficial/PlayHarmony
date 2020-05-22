package iris.playharmony.controller.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iris.playharmony.controller.db.sql.*;
import iris.playharmony.controller.handler.PathHandler;
import iris.playharmony.model.Email;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Role;
import iris.playharmony.model.User;
import iris.playharmony.util.FileUtils;
import iris.playharmony.util.Resources;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class UserDatabaseController extends AbstractDatabaseController implements IUserDatabaseController {

    private static final String USERS_TABLE_NAME = "USERS";

    private static final String SQL_SELECT_ALL_USERS = "SELECT * FROM USERS";

    private static final SQLWriteQuery SQL_QUERY_INSERT_NEW_USER = new SQLInsertQuery(USERS_TABLE_NAME,
            "photo", "name", "surname", "category", "user_role", "email");

    private static final SQLWriteQuery SQL_QUERY_UPDATE_USER = new SQLUpdateQuery(USERS_TABLE_NAME,
            "email",
            "photo", "name", "surname", "category", "user_role", "email");

    private static final SQLWriteQuery SQL_QUERY_REMOVE_USER_BY_EMAIL = new SQLDeleteByKeyQuery(USERS_TABLE_NAME, "email");



    public UserDatabaseController() {
    }

    @Override
    public List<User> getUsers() {

        List<User> userList = new ArrayList<>();

        try(Statement statement = getDBConnection().createStatement()) {

            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_USERS);

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
                    .role(Role.getRoleFrom(resultSet.getString("USER_ROLE")))
                    .mail(new Email(resultSet.getString("EMAIL")))
                    .photo(photo)
                    .setPlayLists(playList);
                    // .setPassword(resultSet.getString("PASSWORD"));

            userList.add(user);
        }
    }

    @Override
    public boolean addUser(User user) {

        checkUser(user);

        if(userExists(user)) {
            throw new IllegalArgumentException("User already exists");
        }

        try(SQLStatement statement = SQL_QUERY_INSERT_NEW_USER.prepareStatement(getDBConnection())) {

            File photo = user.getPhoto();

            FileUtils.readFileBinary(photo, inputStream -> statement.set("photo", inputStream, (int)photo.length()));

            statement.set("name", user.getName())
                    .set("surname", user.getSurname())
                    .set("email", user.getEmail().toString())
                    .set("category", user.getCategory())
                    .set("user_role", user.getRole().toString());

            return statement.execute() != SQLStatement.ERROR_CODE;

        } catch (Exception e) {
            e.printStackTrace();
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

        if(!userExists(user)) {
            throw new IllegalArgumentException("User does not exists");
        }

        try(SQLStatement statement = SQL_QUERY_UPDATE_USER.prepareStatement(getDBConnection())) {

            File photo = user.getPhoto();

            if(photo != null) {
                FileUtils.readFileBinary(photo, inputStream -> statement.set("photo", inputStream, (int)photo.length()));
            } else {
                File defaultPhoto = new File(requireNonNull(Resources.get(PathHandler.DEFAULT_PHOTO_PATH)));
                FileUtils.readFileBinary(defaultPhoto, inputStream -> statement.set("photo", inputStream, inputStream.available()));
            }

            statement.setKey("email", user.getEmail().toString())
                    .set("name", user.getName())
                    .set("surname", user.getSurname())
                    .set("email", user.getEmail().toString())
                    .set("category", user.getCategory())
                    .set("user_role", user.getRole().toString());

            return statement.execute() != SQLStatement.ERROR_CODE;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean removeUser(String userEmail) {

        if(userEmail == null || userEmail.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if(!userExists(userEmail)) {
            throw new IllegalArgumentException("User " + userEmail + " does not exists");
        }

        try(SQLStatement statement = SQL_QUERY_REMOVE_USER_BY_EMAIL.prepareStatement(getDBConnection())) {

            statement.setKey("email", userEmail);

            return statement.execute() != SQLStatement.ERROR_CODE;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean userExists(String userEmail) {
        return getUsers().stream().anyMatch(user -> Objects.equals(user.getEmail().toString(), userEmail));
    }

}