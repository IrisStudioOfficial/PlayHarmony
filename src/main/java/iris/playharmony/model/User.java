package iris.playharmony.model;

public class User {

    private String photo;
    private String surname;
    private String name;
    private String category;
    private Role role;
    private Email email;

    public User(String photo, String surname, String name, String category, Role role, Email email) {
        this.photo = photo;
        this.surname = surname;
        this.name = name;
        this.category = category;
        this.role = role;
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }
}
