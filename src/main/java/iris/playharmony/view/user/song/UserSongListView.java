package iris.playharmony.view.user.song;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.model.player.Spectrum;
import iris.playharmony.session.Session;
import iris.playharmony.view.player.MusicPlayerView;
import iris.playharmony.view.player.MusicPlayerViewModel;
import iris.playharmony.view.template.ListTemplate;
import iris.playharmony.view.user.playlist.SelectPlaylistView;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.animation.Interpolator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;

import java.util.Comparator;

public class UserSongListView extends ListTemplate<ObservableSong> {

    public UserSongListView() {
        super("Search Song");
    }

    @Override
    protected Comparator<ObservableSong> initComparator() {
        return Comparator.comparing(observable -> observable.title().get());
    }

    @Override
    protected ObservableList<ObservableSong> getData() {
        ObservableList<ObservableSong> songs = FXCollections.observableArrayList();
        new DatabaseController()
                .getSongs()
                .stream()
                .map(ObservableSong::from)
                .forEach(songs::add);
        return songs;
    }

    @Override
    protected String fieldToFilter(ObservableSong song) {
        return song.getTitle();
    }

    @Override
    protected TableColumn[] initTable() {
        return new TableColumn[] {
                TableFactory.tableColumnPhoto("", "fav", 40),
                TableFactory.tableColumnPhoto("Photo", "photo", 200),
                TableFactory.tableColumn("Title", "title"),
                TableFactory.tableColumn("Author", "author"),
                TableFactory.tableColumn("Date", "date")
        };
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Add To Playlist", this::selectPlaylist),
                ButtonFactory.button("Add To Favourites", this::addToFavourites),
                ButtonFactory.button("Play song", this::playSong)
        };
    }

    private void selectPlaylist(ActionEvent event) {
        ObservableSong selectedItem = getSelectedItem();
        if (selectedItem != null) {
            NavController.get().pushView(new SelectPlaylistView(selectedItem.getTitle()));
        }
    }

    private void addToFavourites(ActionEvent actionEvent) {
        Playlist favourites = Session.getSession().currentUser().favourites();
        favourites = favourites == null ? new Playlist("Favourites") : favourites;

        ObservableSong selectedItem = getSelectedItem();
        Song toBeAdded = new DatabaseController().getSongs().stream()
                .filter(song -> song.getTitle().equals(selectedItem.getTitle()))
                .findAny().get();

        favourites.addSong(toBeAdded);
        new DatabaseController().addFavourites(favourites, Session.getSession().currentUser());
    }

    private void playSong(ActionEvent actionEvent) {
        MusicPlayer musicPlayer = new MusicPlayer();
        Spectrum spectrum = new Spectrum(Interpolator.LINEAR);
        ObservableSong selectedItem = getSelectedItem();
        Song song = new DatabaseController().getSongs().stream().filter(s -> s.getTitle().equals(selectedItem.getTitle())).findFirst().get();

        MusicPlayerViewModel musicPlayerViewModel = new MusicPlayerViewModel(musicPlayer, spectrum);
        musicPlayerViewModel.setSong(song);

        NavController.get().pushView(new MusicPlayerView(musicPlayerViewModel));
        musicPlayer.play();
    }
}


