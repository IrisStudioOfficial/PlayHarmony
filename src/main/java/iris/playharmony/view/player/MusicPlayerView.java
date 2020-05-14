package iris.playharmony.view.player;

import iris.playharmony.view.player.spectrum.SpectrumView;
import javafx.geometry.Pos;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static java.util.Objects.requireNonNull;

public class MusicPlayerView extends BorderPane {

    private static final Font TITLE_FONT = Font.font(36);


    private final Text title;
    private final SpectrumView spectrumView;
    private final MusicControlPanelView controlPanelView;
    private final MusicPlayerViewModel viewModel;

    public MusicPlayerView(MusicPlayerViewModel viewModel) {

        this.viewModel = requireNonNull(viewModel);
        
        title = createTitle(viewModel);
        spectrumView = createSpectrumView(viewModel);
        controlPanelView = createControlPanelView(viewModel);
    }

    private Text createTitle(MusicPlayerViewModel viewModel) {

        Text title = new Text();

        title.setFont(TITLE_FONT);

        title.setTextAlignment(TextAlignment.CENTER);

        title.textProperty().bind(viewModel.songTitleProperty());

        title.setEffect(new InnerShadow());

        title.setFill(Color.DARKORANGE);

        setTop(title);

        setAlignment(title, Pos.CENTER);

        return title;
    }

    private SpectrumView createSpectrumView(MusicPlayerViewModel viewModel) {

        SpectrumView spectrumView = new SpectrumView(viewModel);

        setCenter(spectrumView);

        return spectrumView;
    }

    private MusicControlPanelView createControlPanelView(MusicPlayerViewModel viewModel) {

        MusicControlPanelView controlPanelView = new MusicControlPanelView(viewModel);

        setBottom(controlPanelView);

        return controlPanelView;
    }
}
