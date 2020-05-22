package iris.playharmony.view.user.song;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.model.player.Spectrum;
import iris.playharmony.session.Session;
import iris.playharmony.util.OnRefresh;
import iris.playharmony.view.player.MusicPlayerView;
import iris.playharmony.view.player.MusicPlayerViewModel;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TableFactory;
import iris.playharmony.view.util.TextFactory;
import javafx.animation.Interpolator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class AddSongToPlaylistView extends VBox {

    private Playlist playlist;
    private ObservableList<ObservableSong> songs;

    private TableView songsTable;
    private Pagination pagination;

    private static int SPACING = 15;

    public AddSongToPlaylistView(Playlist playlist) {
        super(SPACING);
        this.playlist = playlist;
        songs = getSongs();
        initElements();
        setPadding(new Insets(SPACING));
    }

    private void initElements() {
        add(TextFactory.label("Add Song to Playlist", DefaultStyle.label()));

        add(songsTable = TableFactory.table(songs,
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Title", "title"),
                TableFactory.tableColumn("Author", "author"),
                TableFactory.tableColumn("Date", "date")
        ));
        add(pagination = TableFactory.pagination(songs, songsTable));

        add(ButtonFactory.button("Add Song", event -> addSongs()));
        add(ButtonFactory.button("Play Song", this::playSong));
    }

    private void add(Node node) {
        getChildren().add(node);
    }

    private ObservableList<ObservableSong> getSongs() {
        ObservableList<ObservableSong> data = FXCollections.observableArrayList();
        for (Song song : new DatabaseController().getSongs()) {
            ObservableSong observableSong = new ObservableSong().title(song.getTitle()).author(song.getAuthor())
                    .photo(song.getPhoto()).date(song.getDate()).path(song.getPathFile());
            if(playlist.getSongList().stream().noneMatch(s -> s.getTitle().equals(observableSong.getTitle()))) {
                data.add(observableSong);
            }
        }
        return data;
    }

    private void addSongs() {
        ObservableSong selectedItem = (ObservableSong) songsTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            playlist.addSong(new DatabaseController().getSongs().stream()
                    .filter(song -> song.getTitle().equals(selectedItem.getTitle()))
                    .findAny().get());
            new DatabaseController().addPlayList(playlist, Session.getSession().currentUser());
            songs.remove(selectedItem);
            refresh();
        }
    }

    @OnRefresh
    public void refresh() {
        TableFactory.updateTable(songs, songsTable);
        TableFactory.updatePagination(songs, songsTable, pagination);
    }

    private void playSong(ActionEvent actionEvent) {
        MusicPlayer musicPlayer = new MusicPlayer();
        Spectrum spectrum = new Spectrum(Interpolator.LINEAR);
        ObservableSong selectedItem = (ObservableSong) songsTable.getSelectionModel().getSelectedItem();
        Song song = new DatabaseController().getSongs().stream().filter(s -> s.getTitle().equals(selectedItem.getTitle())).findFirst().get();

        MusicPlayerViewModel musicPlayerViewModel = new MusicPlayerViewModel(musicPlayer, spectrum);
        musicPlayerViewModel.setSong(song);

        NavController.get().pushView(new MusicPlayerView(musicPlayerViewModel));
        musicPlayer.play();
    }
}
