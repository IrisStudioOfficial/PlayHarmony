package iris.playharmony.view.player;

import iris.playharmony.view.View;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class MusicControlPanelView extends VBox implements View {

    private static final int SPACING = 30;

    private final SongTimeView timeView;
    private final SongButtonPanel buttonPanel;

    public MusicControlPanelView(MusicPlayerViewModel viewModel) {

        setSpacing(SPACING);

        setAlignment(Pos.CENTER);

        timeView = new SongTimeView(viewModel);

        buttonPanel = new SongButtonPanel(viewModel);

        getChildren().addAll(timeView, buttonPanel);
    }

}
