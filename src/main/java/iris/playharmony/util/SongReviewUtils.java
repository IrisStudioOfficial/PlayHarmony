package iris.playharmony.util;

import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.Song;

public class SongReviewUtils {
    public static double getAverageRating(Song song) {
        return DatabaseController.get()
                .getSongReviews()
                .stream()
                .filter(songReview -> songReview.getSongTitle().equals(song.getTitle()))
                .mapToDouble(songReview -> (double)songReview.getRating())
                .average().orElse(0);
    }
}
