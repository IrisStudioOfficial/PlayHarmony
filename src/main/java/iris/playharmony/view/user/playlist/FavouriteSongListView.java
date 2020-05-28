package iris.playharmony.view.user.playlist;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.*;
import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.model.player.Spectrum;
import iris.playharmony.session.Session;
import iris.playharmony.view.player.MusicPlayerView;
import iris.playharmony.view.player.MusicPlayerViewModel;
import iris.playharmony.view.template.ListTemplate;
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

public class FavouriteSongListView extends ListTemplate<ObservableSong> {

    private MusicPlayerViewModel musicPlayerViewModel;
    private SongPlayMode songPlayMode = SongPlayMode.getDefault();
    private int index = 0;

    public FavouriteSongListView() {
        super("User favorite songs");
    }

    @Override
    protected Comparator<ObservableSong> initComparator() {
        return Comparator.comparing(observable -> observable.title().get());
    }

    @Override
    protected ObservableList<ObservableSong> getData() {
        ObservableList<ObservableSong> observableSongs = FXCollections.observableArrayList();
        User user = Session.getSession().currentUser();
        if(user.favourites() != null) {
            user.favourites().getSongList().stream()
                    .map(ObservableSong::from)
                    .forEach(observableSongs::add);
        }

        return observableSongs;
    }

    @Override
    protected String fieldToFilter(ObservableSong song) {
        return song.getTitle();
    }

    @Override
    protected TableColumn[] initTable() {
        return new TableColumn[] {
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
                ButtonFactory.button("Play all", this::playAll),
                ButtonFactory.button("Play song", this::playSong),
                ButtonFactory.button("Delete song", event -> deleteSong())
        };
    }

    private void playAll(ActionEvent actionEvent) {
        Playlist favourites = Session.getSession().currentUser().favourites();
        MusicPlayer musicPlayer = new MusicPlayer();
        Spectrum spectrum = new Spectrum(Interpolator.LINEAR);
        ObservableSong selectedItem = getSelectedItem();
        Song song;
        if (selectedItem == null)
            song = favourites.getSongList().get(0);
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

    private void playSong(ActionEvent actionEvent) {
        MusicPlayer musicPlayer = new MusicPlayer();
        Spectrum spectrum = new Spectrum(Interpolator.LINEAR);
        ObservableSong selectedItem = getSelectedItem();
        Song song = DatabaseController.get().getSongs().stream().filter(s -> s.getTitle().equals(selectedItem.getTitle())).findFirst().get();

        MusicPlayerViewModel musicPlayerViewModel = new MusicPlayerViewModel(musicPlayer, spectrum);
        musicPlayerViewModel.setSong(song);

        NavController.get().pushView(new MusicPlayerView(musicPlayerViewModel));
        musicPlayer.play();
    }

    private void deleteSong() {
        ObservableSong selectedItem = getSelectedItem();
        if(selectedItem != null) {
            if(AlertFactory.confirmAlert("Remove Song", "Do you want to delete the song?")) {
                Song selectedSong = DatabaseController.get().getSongs().stream()
                        .filter(song -> song.getTitle().equals(selectedItem.getTitle()))
                        .findAny().get();
                User currentUser = Session.getSession().currentUser();
                currentUser.favourites().deleteSong(selectedSong);
                DatabaseController.get().addToFavourites(currentUser.favourites(),
                        currentUser);
                refresh();
            }
        }
    }

    public void nextSong() {
        Playlist favourites = Session.getSession().currentUser().favourites();
        switch(songPlayMode) {
            case SEQUENTIAL:
                index++;
                if(index >= favourites.getSongList().size())
                    index = 0;

                break;
            case RANDOM:
                index = new Random().nextInt(favourites.getSongList().size());
                break;
            case SELF:
                break;
        }

        musicPlayerViewModel.setSong(favourites.getSongList().get(index));
        musicPlayerViewModel.getMusicPlayer().play();
    }

    public void previousSong() {
        Playlist favourites = Session.getSession().currentUser().favourites();
        switch(songPlayMode) {
            case SEQUENTIAL:
                index--;
                if(index < 0)
                    index = favourites.getSongList().size() - 1;

                break;
            case RANDOM:
                index = new Random().nextInt(favourites.getSongList().size());
                break;
            case SELF:
                break;
        }

        musicPlayerViewModel.setSong(favourites.getSongList().get(index));
        musicPlayerViewModel.getMusicPlayer().play();
    }
}