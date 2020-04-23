package iris.playharmony.controller;

import iris.playharmony.controller.handler.PathHandler;
import iris.playharmony.model.Email;
import iris.playharmony.model.Role;
import iris.playharmony.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController {

    private Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + PathHandler.DATABASE_PATH;
            conn = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    public List<User> getUses() {
        List<User> userList = new ArrayList<>();

        String sql = "SELECT * FROM USERS";
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                userList.add(new User(
                        rs.getString("PHOTO"),
                        rs.getString("SURNAME"),
                        rs.getString("NAME"),
                        rs.getString("CATEGORY"),
                        Role.valueOf(rs.getString("ROLE")),
                        new Email(rs.getString("EMAIL"))
                ));
            }
        } catch (SQLException e) {}

        return userList;
    }

    public void setUses(List<User> userList) {

    }

    public void addUser(User user) {

    }

    public void updateUser(User user) {

    }

    public void removeUser(User user) {

    }
}
