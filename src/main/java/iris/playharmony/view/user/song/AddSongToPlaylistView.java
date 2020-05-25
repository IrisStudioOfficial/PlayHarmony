package iris.playharmony.view.user.song;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.model.player.Spectrum;
import iris.playharmony.session.Session;
import iris.playharmony.view.player.MusicPlayerView;
import iris.playharmony.view.player.MusicPlayerViewModel;
import iris.playharmony.view.template.ListTemplate;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.animation.Interpolator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;

import java.util.Comparator;

public class AddSongToPlaylistView extends ListTemplate<ObservableSong> {

    private Playlist playlist;

    public AddSongToPlaylistView(Object baseElement) {
        super("Add Song To Playlist", baseElement);
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
        ObservableList<ObservableSong> lists = FXCollections.observableArrayList();
        for (Song song : new DatabaseController().getSongs()) {
            ObservableSong observableSong = new ObservableSong().title(song.getTitle()).author(song.getAuthor())
                    .photo(song.getPhoto()).date(song.getDate()).path(song.getPathFile());
            if(playlist.getSongList().stream().noneMatch(s -> s.getTitle().equals(observableSong.getTitle()))) {
                lists.add(observableSong);
            }
        }
        return lists;
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
                TableFactory.tableColumn("Date", "date")
        };
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Add Song", event -> addSongs()),
                ButtonFactory.button("Play Song", this::playSong)
        };
    }

    private void addSongs() {
        ObservableSong selectedItem = getSelectedItem();
        if(selectedItem != null) {
            playlist.addSong(new DatabaseController().getSongs().stream()
                    .filter(song -> song.getTitle().equals(selectedItem.getTitle()))
                    .findAny().get());
            new DatabaseController().addPlayList(playlist, Session.getSession().currentUser());
            refresh();
        }
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
