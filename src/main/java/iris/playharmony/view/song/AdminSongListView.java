package iris.playharmony.view.song;

import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.controller.NavController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.model.Song;
import iris.playharmony.view.util.*;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.event.Event;

public class AdminSongListView extends SongListView {

    public AdminSongListView() {
        super();
    }

    @Override
    public void initElements() {
        add(TextFactory.label("Songs", DefaultStyle.title()));
        add(searchForm());
        add(songsTable = TableFactory.table(data,
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Title", "title"),
                TableFactory.tableColumn("Author", "author"),
                TableFactory.tableColumn("Date", "date"),
                TableFactory.tableColumn("Path", "Path")
        ));

        add(pagination = TableFactory.pagination(data, songsTable));
        add(getBottomButtonPanel());
    }

    private static void addSong() {
        NavController.get().pushView(new NewSongView());
    }

    private Node getBottomButtonPanel() {
        Region padding = new Region();
        padding.setPrefWidth(5);
        return new HBox(
                ButtonFactory.button("Add Song", e -> addSong()),
                padding,
                ButtonFactory.button("Delete Song", this::removeSong)
        );
    }

    public void removeSong(Event event) {
        event.consume();
        ObservableSong selection = (ObservableSong) songsTable.getSelectionModel().getSelectedItem();
        if (selection == null)
            return;
        if (!new DatabaseController().deleteSong(new Song().setTitle(selection.getTitle())))
            AlertFactory.errorAlert("ERROR! Couldn't remove song", "ERROR! Couldn't remove song");
        refresh();
    }
}


