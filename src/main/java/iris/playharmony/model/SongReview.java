package iris.playharmony.model;

import org.controlsfx.control.Rating;

public class SongReview {

    private String user;
    private String songTitle;
    private double rating;
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

    public double getRating() {
        return rating;
    }

    public SongReview setRating(double rating) {
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

    public static SongReview from(User user, Song song, Rating rating) {

        return new SongReview()
                .setRating(rating.getRating())
                .setUser(user.getName())
                .setSongTitle(song.getTitle());
    }
}
