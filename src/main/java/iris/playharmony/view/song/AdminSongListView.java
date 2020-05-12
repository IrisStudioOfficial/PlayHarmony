package iris.playharmony.view.song;

import iris.playharmony.controller.NavController;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class AdminSongListView extends SongListView {

    public AdminSongListView() {
        super();
        add(getTitleRow());
        add(searchForm());
        initializeTableView();
        this.pagination = pagination(data, songsTable);
        add(getBottomButtonPanel());
        setPadding(new Insets(SPACING));
    }

    protected Node getTitleRow() {
        HBox titleRow = new HBox(title("Songs"));
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        Region padding = new Region();
        padding.setPrefWidth(5);
        titleRow.getChildren().add(region);
        titleRow.getChildren().add(button("Add Song", event -> {
            NavController.get().pushView(new NewSongView());
        }));
        titleRow.getChildren().add(padding);
        titleRow.getChildren().add(button("Delete Song", this::removeSong));

        return titleRow;
    }
}


