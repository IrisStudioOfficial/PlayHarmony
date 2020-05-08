package iris.playharmony.view.player;

import iris.playharmony.view.View;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import static java.util.Objects.requireNonNull;

public class MusicPlayerView extends VBox implements View {

    private final Label title;
    private final SpectrumView spectrumView;
    private final MusicControlPanelView controlPanelView;
    private final MusicPlayerViewModel viewModel;

    public MusicPlayerView(MusicPlayerViewModel viewModel) {

        this.viewModel = requireNonNull(viewModel);
        
        title = createTitle(viewModel);
        spectrumView = createSpectrumView(viewModel);
        controlPanelView = createControlPanelView(viewModel);
    }

    private Label createTitle(MusicPlayerViewModel viewModel) {

        Label title = new Label();

        title.textProperty().bind(viewModel.);

    }

    private SpectrumView createSpectrumView(MusicPlayerViewModel viewModel) {

    }

    private MusicControlPanelView createControlPanelView(MusicPlayerViewModel viewModel) {

    }
}
