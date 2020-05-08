package iris.playharmony.model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private String name;
    private List<Song> songList;

    public Playlist(String name) {
        this.name = name;
        this.songList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean addSong(Song song) {
        if(!songList.stream().anyMatch(songPlaylist -> songPlaylist.getTitle().equals(song.getTitle()))) {
            return songList.add(song);
        }

        return false;
    }
}
