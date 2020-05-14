package iris.playharmony.model;

public class Song {

    private String title;
    private String author;
    private String photo;
    private String date;
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
}
