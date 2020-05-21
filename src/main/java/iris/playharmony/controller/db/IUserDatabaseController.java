package iris.playharmony.controller.db;

import iris.playharmony.exceptions.CreateUserException;
import iris.playharmony.exceptions.RemoveUserException;
import iris.playharmony.exceptions.UpdateUserException;
import iris.playharmony.model.User;

import java.util.List;

public interface IUserDatabaseController {

    List<User> getUsers();

    boolean addUser(User user) throws CreateUserException;

    boolean updateUser(User user, String key) throws UpdateUserException;

    boolean removeUser(String key) throws RemoveUserException;
}
