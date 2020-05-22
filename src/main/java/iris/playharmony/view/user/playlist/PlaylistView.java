package iris.playharmony.view.user.playlist;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Playlist;
import iris.playharmony.model.Song;
import iris.playharmony.model.SongPlayMode;
import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.model.player.Spectrum;
import iris.playharmony.session.Session;
import iris.playharmony.util.OnRefresh;
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
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;

import java.util.Comparator;
import java.util.Random;

public class PlaylistView extends ListTemplate<ObservableSong> {

    private Playlist playlist;

    private MusicPlayerViewModel musicPlayerViewModel;
    private SongPlayMode songPlayMode = SongPlayMode.getDefault();
    int index = 0;

    public PlaylistView(Playlist playlist) {
        super(playlist.getName());
        this.playlist = playlist;
        init();
    }

    @Override
    protected void initElements() {

    }

    @Override
    protected void searchCommand() {
        if(searchField.getText().isEmpty())
            return;
        data = data.filtered(observableSong -> observableSong.getTitle().toLowerCase().contains(searchField.getText().toLowerCase()));
        TableFactory.updateTable(data, table);
        TableFactory.updatePagination(data, table, pagination);
    }

    @Override
    protected TableView initTable() {
        return TableFactory.table(data,
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Title", "title"),
                TableFactory.tableColumn("Author", "author"),
                TableFactory.tableColumn("Date", "date")
        );
    }

    @Override
    protected Pagination initPagination() {
        return TableFactory.pagination(data, table);
    }

    @Override
    protected Comparator<ObservableSong> getComparator() {
        return Comparator.comparing(o -> o.title().get());
    }

    @Override
    protected ObservableList<ObservableSong> getObservableData() {
        ObservableList<ObservableSong> data = FXCollections.observableArrayList();
        for (Song song : playlist.getSongList()) {
            ObservableSong observableSong = new ObservableSong().title(song.getTitle()).author(song.getAuthor())
                    .photo(song.getPhoto()).date(song.getDate()).path(song.getPathFile());
            data.add(observableSong);
        }
        return data;
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Add Song", event -> addSong()),
                ButtonFactory.button("Delete Song", event -> deleteSong()),
                ButtonFactory.button("Play Song", this::playSong)
        };
    }

    @OnRefresh
    @Override
    public void refresh() {
        data = getObservableData();
        TableFactory.updateTable(data, table);
        TableFactory.updatePagination(data, table, pagination);
    }

    private void addSong() {
        NavController.get().pushView(new AddSongToPlaylistView(playlist));
    }

    private void deleteSong() {
        ObservableSong selectedItem = (ObservableSong) table.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            if(AlertFactory.confirmAlert("Remove Song", "Do you want to delete the song?")) {
                Song songPrueba = new DatabaseController().getSongs().stream()
                        .filter(song -> song.getTitle().equals(selectedItem.getTitle()))
                        .findAny().get();
                playlist.deleteSong(songPrueba);
                new DatabaseController().addPlayList(playlist, Session.getSession().currentUser());
                refresh();
            }
        }
    }

    private void playSong(ActionEvent actionEvent) {
        MusicPlayer musicPlayer = new MusicPlayer();
        Spectrum spectrum = new Spectrum(Interpolator.LINEAR);
        ObservableSong selectedItem = (ObservableSong) table.getSelectionModel().getSelectedItem();
        Song song;
        if (selectedItem == null)
            song = playlist.getSongList().get(0);
        else
            song = new DatabaseController().getSongs().stream().filter(s -> s.getTitle().equals(selectedItem.getTitle())).findFirst().get();

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