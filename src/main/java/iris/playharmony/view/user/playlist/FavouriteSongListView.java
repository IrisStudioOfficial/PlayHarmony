package iris.playharmony.view.user.playlist;

import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Song;
import iris.playharmony.model.SongPlayMode;
import iris.playharmony.model.User;
import iris.playharmony.session.Session;
import iris.playharmony.view.player.MusicPlayerViewModel;
import iris.playharmony.view.template.ListTemplate;
import iris.playharmony.view.user.MusicPlayerController;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;

import java.util.Comparator;

public class FavouriteSongListView extends ListTemplate<ObservableSong> {

    MusicPlayerController musicPlayerController;
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
                ButtonFactory.button("Play all", e -> {
                    musicPlayerController = new MusicPlayerController(Session.getSession().currentUser().favourites());
                    try {
                        musicPlayerController.playSong(ObservableSong.from(Session.getSession().currentUser().favourites().getSongList().get(0)));
                    } catch(Exception ex) {}
                }),
                ButtonFactory.button("Play song", e -> {
                    if(getSelectedItem() != null) {
                        musicPlayerController = new MusicPlayerController(Session.getSession().currentUser().favourites());
                        musicPlayerController.playSong(getSelectedItem());
                    }
                }),
                ButtonFactory.button("Delete song", event -> deleteSong())
        };
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
}