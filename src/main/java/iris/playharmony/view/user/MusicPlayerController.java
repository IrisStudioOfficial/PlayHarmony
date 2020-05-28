package iris.playharmony.view.user;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.model.SongPlayMode;
import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.model.player.Spectrum;
import iris.playharmony.view.player.MusicPlayerView;
import iris.playharmony.view.player.MusicPlayerViewModel;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;

import java.util.Random;

public class MusicPlayerController {
    private SongPlayMode songPlayMode = SongPlayMode.getDefault();
    private MusicPlayerViewModel musicPlayerViewModel;
    private Playlist playlist;

    private int currentSongIndex = 0;

    public MusicPlayerController(Playlist playlist) {
        this.playlist = playlist;
    }

    public void playSong(ObservableSong selectedItem) {
        MusicPlayer musicPlayer = new MusicPlayer();
        Spectrum spectrum = new Spectrum(Interpolator.LINEAR);
        Song song;
        if (selectedItem == null)
            song = playlist.getSongList().get(0);
        else
            song = DatabaseController.get().getSongs().stream().filter(s -> s.getTitle().equals(selectedItem.getTitle())).findFirst().get();

        musicPlayerViewModel = new MusicPlayerViewModel(musicPlayer, spectrum);
        musicPlayerViewModel.setSong(song);

        musicPlayerViewModel.nextSongTriggeredProperty().addListener((a, b, c) -> nextSong());
        musicPlayerViewModel.previousSongTriggeredProperty().addListener((a, b, c) -> previousSong());
        musicPlayerViewModel.songPlayModeProperty().addListener((observable, oldValue, newValue) -> songPlayMode = newValue);
        NavController.get().pushView(new MusicPlayerView(musicPlayerViewModel));
        musicPlayer.play();
    }

    public void nextSong() {
        switch(songPlayMode) {
            case SEQUENTIAL:
                currentSongIndex++;
                if(currentSongIndex >= playlist.getSongList().size())
                    currentSongIndex = 0;

                break;
            case RANDOM:
                currentSongIndex = new Random().nextInt(playlist.getSongList().size());
                break;
            case SELF:
                break;
        }

        musicPlayerViewModel.setSong(playlist.getSongList().get(currentSongIndex));
        musicPlayerViewModel.getMusicPlayer().play();
    }

    public void previousSong() {
        switch(songPlayMode) {
            case SEQUENTIAL:
                currentSongIndex--;
                if(currentSongIndex < 0)
                    currentSongIndex = playlist.getSongList().size() - 1;

                break;
            case RANDOM:
                currentSongIndex = new Random().nextInt(playlist.getSongList().size());
                break;
            case SELF:
                break;
        }

        musicPlayerViewModel.setSong(playlist.getSongList().get(currentSongIndex));
        musicPlayerViewModel.getMusicPlayer().play();
    }
}
