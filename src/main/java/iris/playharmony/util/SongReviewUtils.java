package iris.playharmony.util;

import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.Song;
import iris.playharmony.model.SongReview;
import iris.playharmony.session.Session;
import org.controlsfx.control.Rating;

import java.util.Optional;

public class SongReviewUtils {

    public static double getAverageRating(Song song) {

        return DatabaseController.get()
                .getSongReviews()
                .stream()
                .filter(songReview -> songReview.getSongTitle().equals(song.getTitle()))
                .mapToDouble(songReview -> (double)songReview.getRating())
                .average().orElse(0);
    }

    public static Rating getRatingElement(Song song) {

        Rating rating = new Rating();
        rating.setUpdateOnHover(false);
        rating.setPartialRating(true);
        rating.setMaxHeight(20);
        rating.setRating(SongReviewUtils.getAverageRating(song));
        rating.setOnMouseClicked(event -> onRatingClick(song, rating));

        return rating;
    }

    private static void onRatingClick(Song song, Rating rating) {

        Optional<SongReview> currentUserSongReview =
                DatabaseController.get().getSongReviews().stream()
                .filter(songReview -> songReview.getUser().equals(Session.getSession().currentUser().getName()))
                .filter(songReview -> songReview.getSongTitle().equals(song.getTitle()))
                .findFirst();

        if (currentUserSongReview.isPresent()) {

            SongReview songReview = currentUserSongReview.get();
            songReview.setRating(rating.getRating());
            DatabaseController.get().updateSongReview(songReview);

        } else {

            SongReview songReview = SongReview.from(Session.getSession().currentUser(), song, rating);
            DatabaseController.get().addSongReview(songReview);
        }

        rating.setRating(SongReviewUtils.getAverageRating(song));
    }
}
