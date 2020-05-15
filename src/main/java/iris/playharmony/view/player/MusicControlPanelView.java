package iris.playharmony.view.player;

import javafx.geometry.Pos;
import javafx.scene.DepthTest;
import javafx.scene.layout.VBox;

public class MusicControlPanelView extends VBox {

    private static final int SPACING = 30;
    private static final int MIN_HEIGHT = 120;

    private final SongTimeView timeView;
    private final SongButtonPanel buttonPanel;

    public MusicControlPanelView(MusicPlayerViewModel viewModel) {

        setTranslateZ(-1);

        setSpacing(SPACING);
        setAlignment(Pos.CENTER);
        setMinHeight(MIN_HEIGHT);

        timeView = new SongTimeView(viewModel);

        buttonPanel = new SongButtonPanel(viewModel);

        getChildren().addAll(timeView, buttonPanel);
    }

}
