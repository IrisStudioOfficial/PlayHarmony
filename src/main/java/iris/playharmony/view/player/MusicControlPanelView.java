package iris.playharmony.view.player;

import iris.playharmony.view.View;
import javafx.scene.layout.VBox;

public class MusicControlPanelView extends VBox implements View {

    private static final int SPACING = 10;

    private final SongTimeView timeView;

    public MusicControlPanelView(MusicPlayerViewModel viewModel) {

        setSpacing(SPACING);

        timeView = new SongTimeView(viewModel);

        add(timeView);
    }

}
