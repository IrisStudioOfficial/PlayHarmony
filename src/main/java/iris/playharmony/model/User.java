package iris.playharmony.model;

import java.io.File;

public class User {
    
    private File photo;
    private String name;
    private String surname;
    private String category;
    private Role role;
    private Email email;

    public User() {}

    public User(File photo, String name, String surname, String category, Role role, Email email) {
        this.photo = photo;
        this.name = name;
        this.surname = surname;
        this.category = category;
        this.role = role;
        this.email = email;
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
}
