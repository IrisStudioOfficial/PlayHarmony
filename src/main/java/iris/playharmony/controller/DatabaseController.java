package iris.playharmony.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iris.playharmony.controller.handler.PathHandler;
import iris.playharmony.exceptions.CreateUserException;
import iris.playharmony.exceptions.EmailException;
import iris.playharmony.exceptions.RemoveUserException;
import iris.playharmony.exceptions.UpdateUserException;
import iris.playharmony.model.*;

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

                    List<Playlist> list = new Gson().fromJson(rs.getString("PLAYLIST"), new TypeToken<List<Playlist>>(){}.getType());
                    list = list == null ? new ArrayList<>() : list;

                    userList.add(new User(image.getAbsoluteFile(), rs.getString("NAME"), rs.getString("SURNAME"), rs.getString("CATEGORY"), Role.getRoleFrom(rs.getString("USER_ROLE")), new Email(rs.getString("EMAIL")), list));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException | EmailException ignored) {}

        return userList;
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
        if (getUsers().stream().anyMatch(databaseUser -> databaseUser.getEmail().toString().equals(key))) {
            try {
                String sql = "UPDATE USERS SET PHOTO = ?, NAME = ?, SURNAME = ?, CATEGORY = ?, USER_ROLE = ?, EMAIL = ? " +
                        "WHERE EMAIL = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                if(user.getPhoto() != null) {
                    try (FileInputStream fis = new FileInputStream(user.getPhoto())) {
                        ps.setBinaryStream(1, fis, (int) user.getPhoto().length());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    public boolean addSong(Song song){
        String sql = "INSERT INTO SONGS (title, author, photo, publication, pathFile) VALUES(?,?,?,?,?)";

        try(PreparedStatement pst = connection.prepareStatement(sql)){
            pst.setString(1, song.getTitle());
            pst.setString(2, song.getAuthor());

            try (FileInputStream fis = new FileInputStream(song.getPhoto())) {
                pst.setBinaryStream(3, fis, (int)new File(song.getPhoto()).length());
            } catch (IOException e) {
                e.printStackTrace();
            }

            pst.setString(4, song.getDate());
            pst.setString(5, song.getPathFile());
            return pst.executeUpdate() == 1;
        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteSong(Song song){
        String sql = "DELETE FROM SONGS WHERE title = ?";

        try(PreparedStatement pst = connection.prepareStatement(sql)){
            pst.setString(1, song.getTitle());
            return pst.executeUpdate() == 1;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    public List<Song> getSongs() {
        List<Song> songList = new ArrayList<>();

        String sql = "SELECT * FROM SONGS";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                    File image = File.createTempFile("temp", "img");
                    try (FileOutputStream fos = new FileOutputStream(image)) {
                        byte[] buffer = new byte[1024];
                        InputStream is = rs.getBinaryStream("photo");
                        while(is.read(buffer) > 0) {
                            fos.write(buffer);
                        }
                    }

                    songList.add(new Song()
                            .setTitle(rs.getString("TITLE"))
                            .setDate(rs.getString("PUBLICATION"))
                            .setPathFile(rs.getString("PATHFILE"))
                            .setAuthor(rs.getString("AUTHOR"))
                            .setPhoto(image.getAbsolutePath())
                    );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return songList;
    }

    public boolean addPlayList(Playlist updatedPlaylist, User user){
        user.getPlayLists().removeIf(playlist -> playlist.getName().equals(updatedPlaylist.getName()));
        user.addPlayList(updatedPlaylist);

        String sql = "UPDATE USERS SET PLAYLIST = ? WHERE EMAIL = ?";
        String jsonOfPlayList = new Gson().toJson(user.getPlayLists());

        try(PreparedStatement pst = connection.prepareStatement(sql)){
            pst.setString(1, jsonOfPlayList);
            pst.setString(2, user.getEmail().toString());
            return pst.executeUpdate() == 1;
        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }
}
