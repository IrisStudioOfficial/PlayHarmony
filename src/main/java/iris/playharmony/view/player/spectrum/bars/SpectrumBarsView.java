package iris.playharmony.view.player.spectrum.bars;

import iris.playharmony.model.player.Spectrum;
import iris.playharmony.view.player.spectrum.SpectrumUpdatable;
import javafx.animation.Interpolator;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class SpectrumBarsView extends AnchorPane implements SpectrumUpdatable {

    private static final float BAR_WIDTH = 8.5f;
    private static final float BAR_HEIGHT = 1;
    private static final float BAR_SEPARATION = 10.5f;

    private static final double INTERPOLATION_STEP = 0.45;


    private final SpectrumBarGroup bars;
    private final ReadOnlyObjectProperty<Bounds> boundsProperty;

    public SpectrumBarsView(ReadOnlyObjectProperty<Bounds> boundsProperty) {
        bars = new SpectrumBarGroup(this::onCreateNewSpectrumBar);
        this.boundsProperty = boundsProperty;
    }

    @Override
    public void update(Spectrum spectrum, float deltaTime) {

        Interpolator interpolator = spectrum.getInterpolator();

        final int numBands = spectrum.getNumBands();

        final double maxY = getMaxY();

        final double scale = getBarMaxHeight() / 64.0;

        for(int i = 0;i < numBands;i++) {

            final float magnitude = spectrum.getAudioMagnitude(i);

            SpectrumBar bar = bars.get(i);

            double y = bar.getY();

            double newY = maxY - magnitude * scale;

            newY = interpolator.interpolate(y, newY, INTERPOLATION_STEP);

            if(newY > maxY * 0.88) {
                bar.setFalling();
            } else {
                bar.setRising();
            }

            bar.setHeight(Math.max(maxY - newY, 1.0));

            bar.setY(newY - 1.0);
        }

    }

    private SpectrumBar onCreateNewSpectrumBar() {

        SpectrumBar bar = new SpectrumBar();

        bar.setWidth(BAR_WIDTH);
        bar.setHeight(BAR_HEIGHT);

        bar.setX(bars.size() * BAR_SEPARATION);
        bar.setY(getMaxY());

        bar.setEffect(new Reflection());

        getChildren().add(bar);

        return bar;
    }

    private double getMinY() {
        return boundsProperty.get().getMinY();
    }

    private double getMaxY() {
        return boundsProperty.get().getMaxY();
    }

    private double getBarMaxHeight() {
        return boundsProperty.get().getHeight();
    }
}
