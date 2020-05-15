package iris.playharmony.view.player.spectrum;

import iris.playharmony.util.Resources;
import iris.playharmony.view.player.MusicPlayerViewModel;
import iris.playharmony.view.player.spectrum.bars.SpectrumBarsView;
import iris.playharmony.view.player.spectrum.dancer.SpectrumDancerView;
import iris.playharmony.view.util.Style;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.Arrays;

public class SpectrumView extends Pane {

    private final SpectrumDancerView dancerView;
    private final SpectrumBarsView barsView;
    private final SpectrumUpdater spectrumUpdater;

    public SpectrumView(MusicPlayerViewModel viewModel) {

        dancerView = new SpectrumDancerView(layoutBoundsProperty(), viewModel);

        barsView = new SpectrumBarsView(layoutBoundsProperty());

        spectrumUpdater = new SpectrumUpdater(viewModel);

        spectrumUpdater.getSpectrumUpdatables().addAll(Arrays.asList(dancerView, barsView));

        String backgroundPath = new File(Resources.get("icons/player/background.jpg")).toURI().toString();

        setStyle(Style.backgroundImage(backgroundPath));

        getChildren().addAll(barsView, dancerView);

        spectrumUpdater.start();
    }

}
