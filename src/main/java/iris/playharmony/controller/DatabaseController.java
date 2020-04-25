package iris.playharmony.controller;

import iris.playharmony.controller.handler.PathHandler;
import iris.playharmony.exceptions.CreateUserException;
import iris.playharmony.exceptions.EmailException;
import iris.playharmony.model.Email;
import iris.playharmony.model.Role;
import iris.playharmony.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController {

    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + PathHandler.DATABASE_PATH;
            connection = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();

        String sql = "SELECT * FROM USERS";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                try {
                    Blob blob = rs.getBlob("PHOTO");
                    byte [] array = blob.getBytes(1, (int) blob.length());
                    File photo = File.createTempFile("something-", ".binary", new File(blob.toString()));
                    FileOutputStream out = new FileOutputStream(photo);
                    out.write(array);
                    out.close();

                    userList.add(new User(
                            photo,
                            rs.getString("NAME"),
                            rs.getString("SURNAME"),
                            rs.getString("CATEGORY"),
                            Role.getRoleFrom(rs.getString("USER_ROLE")),
                            new Email(rs.getString("EMAIL"))
                    ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException | EmailException ignored) {}

        return userList;
    }

    public void setUsers(List<User> userList) {

    }

    public boolean addUser(User user) throws CreateUserException {
        if(user.getName().equals("") || user.getSurname().equals("") || user.getCategory().equals(""))
            throw new CreateUserException();

        if(getUsers().stream().anyMatch(databaseUser -> databaseUser.getEmail().equals(user.getEmail()))) {
            return false;
        } else {
            try {
                String sql = "INSERT INTO USERS (PHOTO, NAME, SURNAME, CATEGORY, USER_ROLE, EMAIL) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, user.getPhoto().getAbsolutePath() );
                ps.setString(2, user.getName());
                ps.setString(3, user.getSurname());
                ps.setString(4, user.getCategory());
                ps.setString(5, user.getRole().toString());
                ps.setString(6, user.getEmail().toString());
                close(ps);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public void updateUser(User user) {

    }

    public void removeUser(String email) {

    }
}
