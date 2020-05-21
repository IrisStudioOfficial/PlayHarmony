package iris.playharmony.view.song;

import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.DefaultStyle;
import iris.playharmony.view.util.TableFactory;
import iris.playharmony.view.util.TextFactory;
import javafx.event.ActionEvent;


public class FavouriteSongListView extends SongListView {

    @Override
    protected void initElements() {
        add(TextFactory.label("User favorite songs", DefaultStyle.title()));
        add(searchForm());
        add(songsTable = TableFactory.table(data,
                TableFactory.tableColumnPhoto("Photo", "photo", 100),
                TableFactory.tableColumn("Title", "title"),
                TableFactory.tableColumn("Author", "author"),
                TableFactory.tableColumn("Date", "date")
        ));

        add(pagination = TableFactory.pagination(data, songsTable));
        add(ButtonFactory.button("Play all", this::playAll));
        add(ButtonFactory.button("Play song", this::playSong));
    }

    private void playAll(ActionEvent actionEvent) {
        actionEvent.consume();
    }
}