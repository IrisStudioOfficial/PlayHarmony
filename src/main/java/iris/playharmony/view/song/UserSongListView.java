package iris.playharmony.view.song;

import iris.playharmony.controller.NavController;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class UserSongListView extends SongListView {
    public UserSongListView() {
        super();
        add(searchForm());
        initializeTableView();
        this.pagination = pagination(data, songsTable);
        add(getBottomButtonPanel());
        setPadding(new Insets(SPACING));
    }

    protected HBox getBottomButtonPanel() {
        HBox bottomButtonPanel = super.getBottomButtonPanel();
        Region padding = new Region();
        padding.setPrefWidth(5);
        bottomButtonPanel.getChildren().add(padding);
        bottomButtonPanel.getChildren().add(button("Add to playlist", event -> {
            NavController.get().pushView(new SelectPlaylistView());
        }));

        return bottomButtonPanel;
    }
}


