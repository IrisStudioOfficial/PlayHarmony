package iris.playharmony.view.player.spectrum;

import iris.playharmony.util.Resources;
import iris.playharmony.view.player.MusicPlayerViewModel;
import iris.playharmony.view.player.spectrum.bars.SpectrumBarsView;
import iris.playharmony.view.util.Style;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.Arrays;

public class SpectrumView extends Pane {

    private final SpectrumDancerView dancerView;
    private final SpectrumBarsView barsView;
    private final SpectrumUpdater spectrumUpdater;
    private final VBox verticalLayout;
    private final Pane background;

    public SpectrumView(MusicPlayerViewModel viewModel) {

        dancerView = new SpectrumDancerView(viewModel);

        barsView = new SpectrumBarsView(layoutBoundsProperty());

        spectrumUpdater = new SpectrumUpdater(viewModel);

        spectrumUpdater.getSpectrumUpdatables().addAll(Arrays.asList(dancerView, barsView));

        verticalLayout = new VBox(dancerView, barsView);

        String backgroundPath = new File(Resources.get("icons/player/background.jpg")).toURI().toString();

        background = new Pane();

        background.setEffect(new DropShadow());

        background.setStyle(Style.backgroundImage(backgroundPath));

        getChildren().addAll(new StackPane(background, verticalLayout));

        spectrumUpdater.start();
    }

}
