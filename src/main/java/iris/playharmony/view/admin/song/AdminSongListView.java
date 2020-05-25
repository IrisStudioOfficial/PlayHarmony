package iris.playharmony.view.admin.song;

import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.controller.NavController;
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
        new DatabaseController()
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
                ButtonFactory.button("Delete Song", this::removeSong)
        };
    }

    private static void addSong() {
        NavController.get().pushView(new NewSongView());
    }

    public void removeSong(Event event) {
        event.consume();
        ObservableSong selection = getSelectedItem();
        if (selection == null)
            return;
        if (!new DatabaseController().deleteSong(new Song().setTitle(selection.getTitle())))
            AlertFactory.errorAlert("ERROR! Couldn't remove song", "ERROR! Couldn't remove song");
        refresh();
    }
}