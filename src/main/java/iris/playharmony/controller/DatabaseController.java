package iris.playharmony.controller;

import iris.playharmony.controller.handler.PathHandler;
import iris.playharmony.exceptions.CreateUserException;
import iris.playharmony.exceptions.EmailException;
import iris.playharmony.exceptions.UpdateUserException;
import iris.playharmony.model.Email;
import iris.playharmony.model.Role;
import iris.playharmony.model.User;

import java.io.*;
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
                    File image = File.createTempFile("temp", "img");
                    try (FileOutputStream fos = new FileOutputStream(image)) {
                        byte[] buffer = new byte[1024];
                        InputStream is = rs.getBinaryStream("photo");
                        while(is.read(buffer) > 0) {
                            fos.write(buffer);
                        }
                    }

                    userList.add(new User(
                            image.getAbsoluteFile(),
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
        if (getUsers().stream().noneMatch(databaseUser -> databaseUser.getEmail().toString().equals(user.getEmail().toString()))) {
            try {
                String sql = "INSERT INTO USERS (PHOTO, NAME, SURNAME, CATEGORY, USER_ROLE, EMAIL) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                try (FileInputStream fis = new FileInputStream(user.getPhoto())) {
                    ps.setBinaryStream(1, fis, (int)user.getPhoto().length());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                ps.setString(2, user.getName());
                ps.setString(3, user.getSurname());
                ps.setString(4, user.getCategory());
                ps.setString(5, user.getRole().toString());
                ps.setString(6, user.getEmail().toString());

                ps.executeUpdate();

                close(ps);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean updateUser(User user, String key) throws UpdateUserException {
        if(user.getName().equals("") || user.getSurname().equals("") || user.getCategory().equals(""))
            throw new UpdateUserException();
        List<User> users = getUsers();
        if (getUsers().stream().anyMatch(databaseUser -> databaseUser.getEmail().toString().equals(key))) {
            try {
                String sql = "UPDATE USERS SET PHOTO = ?, NAME = ?, SURNAME = ?, CATEGORY = ?, USER_ROLE = ?, EMAIL = ? " +
                        "WHERE EMAIL = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                try (FileInputStream fis = new FileInputStream(user.getPhoto())) {
                    ps.setBinaryStream(1, fis, (int)user.getPhoto().length());

                } catch (IOException e) {
                    e.printStackTrace();
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

    public void removeUser(String email) {

    }
}
