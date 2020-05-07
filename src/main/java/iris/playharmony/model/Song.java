package iris.playharmony.model;

import java.io.File;

public class Song {
    private String title;
    private String author;
    private File photo;
    private String date;
    private String pathFile;

    public Song(String title, String author, File photo, String date, String pathFile) {
        this.title = title;
        this.author = author;
        this.photo = photo;
        this.date = date;
        this.pathFile = pathFile;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public File getPhoto() {
        return photo;
    }

    public String getDate() {
        return date;
    }

    public String getPathFile() {
        return pathFile;
    }
}
