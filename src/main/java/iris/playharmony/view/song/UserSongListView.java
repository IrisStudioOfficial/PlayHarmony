package iris.playharmony.view.song;

import javafx.geometry.Insets;
import javafx.scene.control.Pagination;

public class UserSongListView extends SongListView {
    public UserSongListView() {
        super();
        add(searchForm());
        initializeTableView();
        this.pagination = pagination(data, songsTable);
        add(getBottomButtonPanel());
        setPadding(new Insets(SPACING));
    }
}


