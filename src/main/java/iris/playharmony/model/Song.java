package iris.playharmony.model;

import iris.playharmony.controller.db.DBAttribute;

import java.util.Objects;

public class Song {

    @DBAttribute(index = 1)
    private String title;
    @DBAttribute(index = 2)
    private String author;
    // @DBAttribute(index = 3) We set binary, not string
    private String photo;
    @DBAttribute(index = 4)
    private String date;
    @DBAttribute(index = 5)
    private String pathFile;

    public Song(String title, String author, String photo, String date, String pathFile) {
        this.title = title;
        this.author = author;
        this.photo = photo;
        this.date = date;
        this.pathFile = pathFile;
    }

    public Song() {}

    public Song setTitle(String title) {
        this.title = title;
        return this;
    }

    public Song setAuthor(String author) {
        this.author = author;
        return this;
    }

    public Song setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public Song setDate(String date) {
        this.date = date;
        return this;
    }

    public Song setPathFile(String pathFile) {
        this.pathFile = pathFile;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDate() {
        return date;
    }

    public String getPathFile() {
        return pathFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return title.equals(song.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", photo='" + photo + '\'' +
                ", date='" + date + '\'' +
                ", pathFile='" + pathFile + '\'' +
                '}';
    }
}
