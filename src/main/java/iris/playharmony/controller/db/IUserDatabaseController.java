package iris.playharmony.controller.db;

import iris.playharmony.model.User;

import java.util.List;

public interface IUserDatabaseController {

    List<User> getUsers();

    boolean addUser(User user);

    boolean updateUser(User user, String key);

    boolean updateMyAccount(User user, String key);

    boolean removeUser(String key);
}
