package iris.playharmony.view.player;

import iris.playharmony.model.Song;
import iris.playharmony.model.SongPlayMode;
import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.model.player.Spectrum;
import iris.playharmony.util.ImageFactory;
import iris.playharmony.util.MediaFactory;
import iris.playharmony.util.Resources;
import javafx.beans.property.*;
import javafx.scene.image.Image;

public class MusicPlayerViewModel {

    private final MusicPlayer musicPlayer;

    private final Spectrum spectrum;

    private final ObjectProperty<Song> songProperty;
    private final StringProperty songTitleProperty;
    private final ObjectProperty<Image> songImageProperty;
    private final ObjectProperty<SongPlayMode> songPlayModeProperty;

    public MusicPlayerViewModel(MusicPlayer musicPlayer, Spectrum spectrum) {

        this.musicPlayer = musicPlayer;
        this.spectrum = spectrum;

        musicPlayer.setSpectrumListener(spectrum);
        musicPlayer.setSpectrumNumBands(spectrum.getNumBands());
        musicPlayer.setSpectrumInterval(spectrum.getInterval());
        musicPlayer.setSpectrumThreshold(spectrum.getThreshold());

        songProperty = new SimpleObjectProperty<>();
        songTitleProperty = new SimpleStringProperty();
        songImageProperty = new SimpleObjectProperty<>();
        songPlayModeProperty = new SimpleObjectProperty<>(SongPlayMode.getDefault());
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
        songImageProperty.set(ImageFactory.loadFromFile(Resources.get(song.getPhoto())));
        musicPlayer.setSong(MediaFactory.getMedia(Resources.get(song.getPathFile())));
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

    public ObjectProperty<SongPlayMode> songPlayModeProperty() {
        return songPlayModeProperty;
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public Spectrum getSpectrum() {
        return spectrum;
    }
}
