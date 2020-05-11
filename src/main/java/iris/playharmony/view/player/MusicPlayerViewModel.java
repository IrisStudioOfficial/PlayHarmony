package iris.playharmony.view.player;

import iris.playharmony.model.Song;
import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.util.ImageFactory;
import iris.playharmony.util.MediaFactory;
import javafx.beans.property.*;
import javafx.scene.image.Image;

public class MusicPlayerViewModel {

    private final MusicPlayer musicPlayer;

    private final ObjectProperty<Song> songProperty;
    private final StringProperty songTitleProperty;
    private final ObjectProperty<Image> songImageProperty;

    public MusicPlayerViewModel(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
        songProperty = new SimpleObjectProperty<>();
        songTitleProperty = new SimpleStringProperty();
        songImageProperty = new SimpleObjectProperty<>();
    }

    public Song getSong() {
        return songProperty.get();
    }

    public ReadOnlyObjectProperty<Song> songProperty() {
        return songProperty;
    }

    public void setSong(Song song) {
        songProperty.set(song);
        songTitleProperty.set(song.getTitle());
        songImageProperty.set(ImageFactory.loadFromFile(song.getPhoto()));
        musicPlayer.setSong(MediaFactory.getMediaFromSong(song));
    }

    public String getSongTitle() {
        return songTitleProperty.get();
    }

    public ReadOnlyStringProperty songTitleProperty() {
        return songTitleProperty;
    }

    public Image getSongImage() {
        return songImageProperty.get();
    }

    public ReadOnlyObjectProperty<Image> songImageProperty() {
        return songImageProperty;
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }
}
