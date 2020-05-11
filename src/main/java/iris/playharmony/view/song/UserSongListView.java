package iris.playharmony.view.song;

import javafx.geometry.Insets;

public class UserSongListView extends SongListView {
    public UserSongListView() {
        super();
        add(searchForm());
        initializeTableView();
        add(getPagination());
        add(getBottomButtonPanel());
        setPadding(new Insets(SPACING));
    }
}


