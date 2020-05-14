package iris.playharmony.view.song;

import iris.playharmony.controller.NavController;
import iris.playharmony.model.ObservableSong;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;


public class UserSongListView extends SongListView {
    public UserSongListView() {
        super();
        this.getChildren().add(searchForm());
        this.getChildren().add(initializeTableView());
        this.pagination = TableFactory.pagination(data, songsTable);
        this.getChildren().add(getBottomButtonPanel());
        setPadding(new Insets(SPACING));
    }

    protected HBox getBottomButtonPanel() {
        HBox bottomButtonPanel = super.getBottomButtonPanel();
        Region padding = new Region();
        padding.setPrefWidth(5);
        bottomButtonPanel.getChildren().add(padding);
        bottomButtonPanel.getChildren().add(ButtonFactory.button("Add to playlist", this::selectPlaylist));

        return bottomButtonPanel;
    }

    private void selectPlaylist(ActionEvent event) {
        ObservableSong selectedItem = (ObservableSong) songsTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
            NavController.get().pushView(new SelectPlaylistView(selectedItem.getTitle()));
    }
}


