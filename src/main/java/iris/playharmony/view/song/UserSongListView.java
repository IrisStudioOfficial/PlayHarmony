package iris.playharmony.view.song;

import iris.playharmony.controller.NavController;
import iris.playharmony.model.ObservableSong;
import javafx.geometry.Insets;
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
            ObservableSong selectedItem = (ObservableSong) songsTable.getSelectionModel().getSelectedItem();
            if(selectedItem != null)
                NavController.get().pushView(new SelectPlaylistView(selectedItem.getTitle()));
        }));

        return bottomButtonPanel;
    }
}


