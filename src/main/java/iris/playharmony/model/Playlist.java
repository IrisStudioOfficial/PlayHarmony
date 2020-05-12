package iris.playharmony.model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private String name;
    private List<Song> songList;
    private SongPlayMode songPlayMode;

    public Playlist(String name) {
        this.name = name;
        this.songList = new ArrayList<>();
        this.songPlayMode = SongPlayMode.SEQUENTIAL;
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

    public SongPlayMode getSongPlayMode() {
        return songPlayMode;
    }

    public boolean addSong(Song song) {
        if(!songList.stream().anyMatch(songPlaylist -> songPlaylist.getTitle().equals(song.getTitle()))) {
            return songList.add(song);
        }

        return false;
    }

    public int getSize() {
        return songList.size();
    }

    public void changeSongPlayMode() {
        if(songPlayMode.equals(SongPlayMode.SEQUENTIAL)) {
            songPlayMode = SongPlayMode.RANDOM;
        } else if(songPlayMode.equals(SongPlayMode.RANDOM)) {
            songPlayMode = SongPlayMode.SELF;
        } else {
            songPlayMode = SongPlayMode.SEQUENTIAL;
        }
    }
}
