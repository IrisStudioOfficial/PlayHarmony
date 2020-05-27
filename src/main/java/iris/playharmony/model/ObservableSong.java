package iris.playharmony.model;

import iris.playharmony.session.Session;
import iris.playharmony.util.SongReviewUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.Rating;

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
    private Rating rating = null;

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

        ObservableSong observableSong = new ObservableSong()
                .title(song.getTitle())
                .author(song.getAuthor())
                .date(song.getDate())
                .photo(song.getPhoto())
                .path(song.getPathFile())
                .rating(SongReviewUtils.getRatingElement(song))
                .fav(null);

        if(Session.getSession().currentUser() != null) {
            if (Session.getSession().currentUser().favourites() != null) {
                observableSong.fav(isFavouriteSong(song) ? starIcon() : null);
            }
        }

        return observableSong;
    }

    private static boolean isFavouriteSong(Song song) {
        return Session.getSession().currentUser().favourites().getSongList().contains(song);
    }

    private static ImageView starIcon() {
        return new ImageView(new Image(getResource("icons/star.png").toURI().toString(),
                25, 25, false, false));
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

    public ObservableSong fav(ImageView fav) {
        this.fav = fav;
        return this;
    }

    private static File getResource(String resourcesPath) {
        URL url = ObservableSong.class.getClassLoader().getResource(resourcesPath);
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        } finally {
            return file;
        }
    }

    public ObservableSong rating(Rating rating) {
        this.rating = rating;
        return this;
    }

    public Rating getRating() {
        return this.rating;
    }
}
