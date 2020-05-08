package iris.playharmony.util;

import iris.playharmony.model.Song;
import javafx.scene.media.Media;

public class MediaFactory {

    public static Media getMediaFromSong(Song song) {
        return new Media(getSongPath(song));
    }

    private static String getSongPath(Song song) {

        String path = null;

        try {

            path = MediaFactory.class.getResource(song.getPathFile()).toURI().toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }

}
