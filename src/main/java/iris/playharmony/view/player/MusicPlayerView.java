package iris.playharmony.view.player;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import static java.util.Objects.requireNonNull;

public class MusicPlayerView extends BorderPane {

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

        title.textProperty().bind(viewModel.songTitleProperty());

        setTop(title);

        return title;
    }

    private SpectrumView createSpectrumView(MusicPlayerViewModel viewModel) {

        SpectrumView spectrumView = new SpectrumView();

        spectrumView.setStyle("-fx-background-color: blue;");

        // TODO...

        setCenter(spectrumView);

        return spectrumView;
    }

    private MusicControlPanelView createControlPanelView(MusicPlayerViewModel viewModel) {

        MusicControlPanelView controlPanelView = new MusicControlPanelView(viewModel);

        // TODO...

        setBottom(controlPanelView);

        return controlPanelView;
    }
}
