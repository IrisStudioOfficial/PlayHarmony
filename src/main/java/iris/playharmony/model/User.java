package iris.playharmony.model;

import iris.playharmony.controller.db.DBAttribute;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class User {

    // @DBAttribute(index = 1) We set binary, not string
    private File photo;
    @DBAttribute(index = 2)
    private String name;
    @DBAttribute(index = 3)
    private String surname;
    @DBAttribute(index = 4)
    private String category;
    @DBAttribute(index = 5)
    private Role role;
    @DBAttribute(index = 6)
    private Email email;
    @DBAttribute(index = 7)
    private List<Playlist> playLists;
    // @DBAttribute(index = 8)
    private String password;
    private Playlist favorites = null;

    public User() {}

    public User(File photo, String name, String surname, String category, Role role, Email email, List<Playlist> playLists) {
        this.photo = photo;
        this.name = name;
        this.surname = surname;
        this.category = category;
        this.role = role;
        this.email = email;
        this.playLists = playLists;
    }

    public File getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCategory() {
        return category;
    }

    public Role getRole() {
        return role;
    }

    public Email getEmail() {
        return email;
    }

    public User photo(File photo) {
        this.photo = photo;
        return this;
    }

    public User name(String name) {
        this.name = name;
        return this;
    }

    public User surname(String surname) {
        this.surname = surname;
        return this;
    }

    public User category(String category) {
        this.category = category;
        return this;
    }

    public User role(Role role) {
        this.role = role;
        return this;
    }

    public User mail(Email email) {
        this.email = email;
        return this;
    }

    public User favourites(Playlist playlist) {
        this.favorites = playlist;
        return this;
    }

    public Playlist favourites() {
        return this.favorites;
    }

    public List<Playlist> getPlayLists() {
        return playLists;
    }

    public void addPlayList(Playlist playlist){
        playLists.add(playlist);
    }

    public User setPlayLists(List<Playlist> playLists) {
        this.playLists = playLists;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "photo=" + photo +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", category='" + category + '\'' +
                ", role=" + role +
                ", email=" + email +
                ", playLists=" + playLists +
                ", password='" + password + '\'' +
                '}';
    }
}
