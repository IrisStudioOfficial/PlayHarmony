package iris.playharmony.view.player;

import iris.playharmony.model.Song;
import iris.playharmony.model.player.MusicPlayer;
import javafx.beans.property.ObjectProperty;

public abstract class MusicPlayerViewModel {

    private final MusicPlayer musicPlayer;

    private final ObjectProperty<Song> songProperty;


    public MusicPlayerViewModel(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

}
