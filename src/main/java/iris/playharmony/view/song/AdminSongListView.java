package iris.playharmony.view.song;

import iris.playharmony.controller.NavController;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.LabelFactory;
import iris.playharmony.view.util.TableFactory;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class AdminSongListView extends SongListView {

    public AdminSongListView() {
        super();
        this.getChildren().add(getTitleRow());
        this.getChildren().add(searchForm());
        initializeTableView();
        this.pagination = TableFactory.pagination(data, songsTable);
        this.getChildren().add(getBottomButtonPanel());
        setPadding(new Insets(SPACING));
    }

    private static void addSong(ActionEvent event) {
        NavController.get().pushView(new NewSongView());
    }

    protected Node getTitleRow() {
        HBox titleRow = new HBox(LabelFactory.label("Songs"));
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        Region padding = new Region();
        padding.setPrefWidth(5);
        titleRow.getChildren().add(region);
        titleRow.getChildren().add(ButtonFactory.button("Add Song", AdminSongListView::addSong));
        titleRow.getChildren().add(padding);
        titleRow.getChildren().add(ButtonFactory.button("Delete Song", this::removeSong));

        return titleRow;
    }
}


