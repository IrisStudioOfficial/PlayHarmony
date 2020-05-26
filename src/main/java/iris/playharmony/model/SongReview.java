package iris.playharmony.model;

public class SongReview {
    private String user;
    private String songTitle;
    private int rating;
    private int id;

    public String getUser() {
        return user;
    }

    public SongReview setUser(String user) {
        this.user = user;
        return this;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public SongReview setSongTitle(String songTitle) {
        this.songTitle = songTitle;
        return this;
    }

    public int getRating() {
        return rating;
    }

    public SongReview setRating(int rating) {
        this.rating = rating;
        return this;
    }

    public int getId() {
        return id;
    }

    public SongReview setId(int id) {
        this.id = id;
        return this;
    }
}
