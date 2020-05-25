package iris.playharmony.model;

import iris.playharmony.session.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class ObservableSong {

    private ImageView photo;
    private SimpleStringProperty title = new SimpleStringProperty();
    private SimpleStringProperty author = new SimpleStringProperty();
    private SimpleStringProperty date = new SimpleStringProperty();
    private SimpleStringProperty path = new SimpleStringProperty();
    private ImageView fav = null;

    public ObservableSong photo(String photo) {
        this.photo = new ImageView(new Image(new File(photo).toURI().toString(), 100, 100, false, false));
        return this;
    }

    public ObservableSong title(String title) {
        this.title.set(title);
        return this;
    }

    public ObservableSong author(String author) {
        this.author.set(author);
        return this;
    }

    public ObservableSong date(String date) {
        this.date.set(date);
        return this;
    }

    public ObservableSong path(String path) {
        this.path.set(path);
        return this;
    }

    public ImageView photo() {
        return photo;
    }

    public SimpleStringProperty title() {
        return title;
    }

    public SimpleStringProperty author() {
        return author;
    }

    public SimpleStringProperty date() {
        return date;
    }

    public static ObservableSong from(Song song) {
        return new ObservableSong()
                .title(song.getTitle())
                .author(song.getAuthor())
                .date(song.getDate())
                .photo(song.getPhoto())
                .path(song.getPathFile())
                .fav(Session.getSession().currentUser().favourites().getSongList().contains(song) ? "C:\\Users\\omark\\OneDrive\\Desktop\\PlayHarmony\\src\\main\\resources\\icons\\star.png" : null);
    }

    public ImageView getPhoto() {
        return photo;
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getAuthor() {
        return author.get();
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public String getPath() {
        return path.get();
    }

    public ImageView getFav() {
        return fav;
    }

    public ObservableSong fav(String fav) {
        if(fav != null)
            this.fav = new ImageView(new Image(getResource("icons/star.png").toURI().toString(), 25, 25, false, false));
        return this;
    }

    private File getResource(String resourcesPath) {
        URL url = this.getClass().getClassLoader().getResource(resourcesPath);
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        } finally {
            return file;
        }
    }
}
