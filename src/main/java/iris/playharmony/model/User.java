package iris.playharmony.model;

import java.io.File;

public class User {

    private final File photo;
    private final String name;
    private final String surname;
    private final String category;
    private final Role role;
    private final Email email;

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
}
