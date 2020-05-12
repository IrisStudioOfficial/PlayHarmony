package iris.playharmony.util;

import iris.playharmony.model.Song;
import javafx.scene.media.Media;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

public class MediaFactory {

    public static Media getMedia(String file) {
        try {
            return new Media(new File(file).toURI().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
