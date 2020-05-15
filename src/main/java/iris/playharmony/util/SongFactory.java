package iris.playharmony.util;

import iris.playharmony.model.Song;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SongFactory {

    private static final String SONG_METADATA_FILENAME = "song.json";

    public static Song createSongFromFolder(String path) {

        Path songPath = Paths.get(path);

        if(Files.notExists(songPath)) {
            throw new IllegalArgumentException("Folder " + path + "does not exists");
        }

        String songDataFile = songPath.resolve(SONG_METADATA_FILENAME).toString();

        return Json.fromJsonFile(Song.class, songDataFile);
    }

}
