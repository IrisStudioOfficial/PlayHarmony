package iris.playharmony.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ObservableUser {
    private ImageView photo;
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty surname = new SimpleStringProperty();
    private SimpleStringProperty category = new SimpleStringProperty();
    private SimpleStringProperty role = new SimpleStringProperty();
    private SimpleStringProperty email = new SimpleStringProperty();

    public ImageView getPhoto() {
        return photo;
    }

    public ObservableUser photo(ImageView photo) {
        this.photo = photo;
        return this;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public ObservableUser name(String name) {
        this.name.set(name);
        return this;
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public ObservableUser surname(String surname) {
        this.surname.set(surname);
        return this;
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public ObservableUser category(String category) {
        this.category.set(category);
        return this;
    }

    public String getRole() {
        return role.get();
    }

    public SimpleStringProperty roleProperty() {
        return role;
    }

    public ObservableUser role(String role) {
        this.role.set(role);
        return this;
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public ObservableUser email(String email) {
        this.email.set(email);
        return this;
    }

    public static ObservableUser from(User user) {
        return new ObservableUser()
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail().toString())
                .role(user.getRole().name())
                .category(user.getCategory())
                .photo(new ImageView(new Image(user.getPhoto().toURI().toString(), 100, 100, false, false)));
    }
}
