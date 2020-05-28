package iris.playharmony.view.user.playlist;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.model.SongPlayMode;
import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.model.player.Spectrum;
import iris.playharmony.session.Session;
import iris.playharmony.view.player.MusicPlayerView;
import iris.playharmony.view.player.MusicPlayerViewModel;
import iris.playharmony.view.template.ListTemplate;
import iris.playharmony.view.user.song.AddSongToPlaylistView;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.animation.Interpolator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;

import java.util.Comparator;
import java.util.Random;

public class PlaylistView extends ListTemplate<ObservableSong> {

    private Playlist playlist;

    private MusicPlayerViewModel musicPlayerViewModel;
    private SongPlayMode songPlayMode = SongPlayMode.getDefault();
    int index = 0;

    public PlaylistView(Playlist playlist) {
        super(playlist.getName(), playlist);
    }

    @Override
    protected void initBaseElement(Object playlist) {
        this.playlist = (Playlist) playlist;
    }

    @Override
    protected Comparator<ObservableSong> initComparator() {
        return Comparator.comparing(o -> o.title().get());
    }

    @Override
    protected ObservableList<ObservableSong> getData() {
        ObservableList<ObservableSong> data = FXCollections.observableArrayList();
        playlist.getSongList().stream()
                .map(ObservableSong::from)
                .forEach(data::add);
        return data;
    }

    @Override
    protected String fieldToFilter(ObservableSong song) {
        return song.getTitle();
    }

    @Override
    protected TableColumn[] initTable() {
        return new TableColumn[] {
                TableFactory.tableColumnPhoto("", "fav", 40),
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Title", "title"),
                TableFactory.tableColumn("Author", "author"),
                TableFactory.tableColumn("Date", "date"),
                TableFactory.tableColumn("Avg. Rating", "rating")
        };
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Add Song", event -> addSong()),
                ButtonFactory.button("Delete Song", event -> deleteSong()),
                ButtonFactory.button("Play Song", this::playSong)
        };
    }

    private void addSong() {
        NavController.get().pushView(new AddSongToPlaylistView(playlist));
    }

    private void deleteSong() {
        ObservableSong selectedItem = getSelectedItem();
        if(selectedItem != null) {
            if(AlertFactory.confirmAlert("Remove Song", "Do you want to delete the song?")) {
                Song songPrueba = DatabaseController.get().getSongs().stream()
                        .filter(song -> song.getTitle().equals(selectedItem.getTitle()))
                        .findAny().get();
                playlist.deleteSong(songPrueba);
                DatabaseController.get().addPlayList(playlist, Session.getSession().currentUser());
                refresh();
            }
        }
    }

    private void playSong(ActionEvent actionEvent) {
        MusicPlayer musicPlayer = new MusicPlayer();
        Spectrum spectrum = new Spectrum(Interpolator.LINEAR);
        ObservableSong selectedItem = getSelectedItem();
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
                index++;
                if(index >= playlist.getSongList().size())
                    index = 0;

                break;
            case RANDOM:
                index = new Random().nextInt(playlist.getSongList().size());
                break;
            case SELF:
                break;
        }

        musicPlayerViewModel.setSong(playlist.getSongList().get(index));
        musicPlayerViewModel.getMusicPlayer().play();
    }

    public void previousSong() {
        switch(songPlayMode) {
            case SEQUENTIAL:
                index--;
                if(index < 0)
                    index = playlist.getSongList().size() - 1;

                break;
            case RANDOM:
                index = new Random().nextInt(playlist.getSongList().size());
                break;
            case SELF:
                break;
        }

        musicPlayerViewModel.setSong(playlist.getSongList().get(index));
        musicPlayerViewModel.getMusicPlayer().play();
    }
}