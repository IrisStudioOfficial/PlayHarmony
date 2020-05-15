package iris.playharmony.view.song;

import iris.playharmony.controller.NavController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TableFactory;
import iris.playharmony.view.util.TextFactory;
import javafx.event.ActionEvent;

public class UserSongListView extends SongListView {

    public UserSongListView() {
        super();
    }

    @Override
    public void initElements() {
        add(TextFactory.label("UserSongListView", DefaultStyle.title()));
        add(searchForm());
        add(songsTable = TableFactory.table(data,
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Title", "title"),
                TableFactory.tableColumn("Author", "author"),
                TableFactory.tableColumn("Date", "date")
        ));

        add(pagination = TableFactory.pagination(data, songsTable));
        add(ButtonFactory.button("Add To Playlist", this::selectPlaylist));
    }

    private void selectPlaylist(ActionEvent event) {
        ObservableSong selectedItem = (ObservableSong) songsTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
            NavController.get().pushView(new SelectPlaylistView(selectedItem.getTitle()));
    }
}


