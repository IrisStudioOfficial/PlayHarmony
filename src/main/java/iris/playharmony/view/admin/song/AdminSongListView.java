package iris.playharmony.view.admin.song;

import iris.playharmony.controller.NavController;
import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Song;
import iris.playharmony.view.template.ListTemplate;
import iris.playharmony.view.util.AlertFactory;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AdminSongListView extends ListTemplate<ObservableSong> {

    public AdminSongListView() {
        super("Songs");
    }

    @Override
    protected Comparator<ObservableSong> initComparator() {
        return Comparator.comparing(o -> o.title().get());
    }

    @Override
    protected ObservableList<ObservableSong> getData() {
        ObservableList<ObservableSong> observableSongs = FXCollections.observableArrayList();
        DatabaseController.get()
                .getSongs()
                .stream()
                .map(ObservableSong::from)
                .forEach(observableSongs::add);
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
                TableFactory.tableColumn("Path", "Path")
        };
    }

    @Override
    protected Node[] bottomButtonPanel() {
        return new Node[] {
                ButtonFactory.button("Add Song", e -> addSong()),
                ButtonFactory.button("Delete Song", this::removeSong),
                ButtonFactory.button("Update Song", e -> updateSong())
        };
    }

    private void addSong() {
        NavController.get().pushView(new NewSongView());
    }

    public void removeSong(Event event) {
        event.consume();
        ObservableSong selection = getSelectedItem();
        if (selection == null)
            return;
        if(AlertFactory.confirmAlert("Delete Song", "Do you want to delete the song?")) {
            if (!DatabaseController.get().deleteSong(new Song().setTitle(selection.getTitle())))
                AlertFactory.errorAlert("ERROR! Couldn't remove song", "ERROR! Couldn't remove song");
            refresh();
        }
    }

    private void updateSong() {
        ObservableSong observableSong = getSelectedItem();
        if(observableSong != null) {
            List<Song> songList = DatabaseController.get().getSongs().stream()
                    .filter(song -> song.getTitle().equals(observableSong.getTitle()))
                    .collect(Collectors.toList());
            if(songList.size() > 0) {
                NavController.get().pushView(new UpdateSongView(songList.get(0)));
            }
        }
    }
}