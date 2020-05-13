package iris.playharmony.view.player.spectrum;

import iris.playharmony.view.player.MusicPlayerViewModel;
import iris.playharmony.view.player.spectrum.bars.SpectrumBarsView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Arrays;

public class SpectrumView extends Pane {

    private final SpectrumDancerView dancerView;
    private final SpectrumBarsView barsView;
    private final SpectrumUpdater spectrumUpdater;

    public SpectrumView(MusicPlayerViewModel viewModel) {

        dancerView = new SpectrumDancerView(viewModel);

        barsView = new SpectrumBarsView(layoutBoundsProperty());

        spectrumUpdater = new SpectrumUpdater(viewModel);

        spectrumUpdater.getSpectrumUpdatables().addAll(Arrays.asList(dancerView, barsView));

        getChildren().addAll(new VBox(dancerView, barsView));

        spectrumUpdater.start();
    }

}
