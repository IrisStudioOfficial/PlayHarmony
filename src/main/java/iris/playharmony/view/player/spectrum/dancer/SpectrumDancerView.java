package iris.playharmony.view.player.spectrum.dancer;

import iris.playharmony.model.player.Spectrum;
import iris.playharmony.view.player.MusicPlayerViewModel;
import iris.playharmony.view.player.spectrum.SpectrumUpdatable;
import javafx.animation.Interpolator;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.DepthTest;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static javafx.scene.media.MediaPlayer.Status.PLAYING;

public class SpectrumDancerView extends AnchorPane implements SpectrumUpdatable {

    private static final int MAX_DANCER_ARC_COUNT = 32;
    private static final double MIN_RADIUS = 80;

    private static final List<Color> COLORS = createColors();

    private final MusicPlayerViewModel viewModel;
    private final Circle songImageFrame;
    private final SpectrumDancerArcGroup arcs;

    public SpectrumDancerView(ReadOnlyObjectProperty<Bounds> parentLayoutBounds, MusicPlayerViewModel viewModel) {

        setDepthTest(DepthTest.ENABLE);

        this.viewModel = viewModel;

        songImageFrame = new Circle();

        songImageFrame.setTranslateZ(-0.1);

        songImageFrame.setStroke(Color.TRANSPARENT);

        parentLayoutBounds.addListener((observable, oldValue, newValue) -> {
            songImageFrame.setTranslateX(newValue.getMaxX() / 2.0);
            songImageFrame.setTranslateY(newValue.getMinY() + songImageFrame.getRadius() * 2.25);
        });

        viewModel.songImageProperty().addListener((observable, oldValue, newValue) -> songImageFrame.setFill(new ImagePattern(newValue)));

        songImageFrame.setRadius(MIN_RADIUS);

        songImageFrame.setEffect(new InnerShadow());

        arcs = new SpectrumDancerArcGroup(this::onCreateSpectrumDancerBar);

        getChildren().add(songImageFrame);
    }

    @Override
    public void update(Spectrum spectrum, float deltaTime) {

        final float[] audioData = spectrum.getAudioData();

        final float beat = getAverage(audioData, 10);

        float average = getAverage(audioData, spectrum.getNumBands());

        Interpolator interpolator = Interpolator.LINEAR;

        final double interpolationStep = 0.5;

        songImageFrame.setRadius(interpolator.interpolate(songImageFrame.getRadius(), beat * 0.5 + MIN_RADIUS, 0.8));

        final float minRadius = (float) songImageFrame.getRadius();

        float size = minRadius * 0.8f;

        final float center = spectrum.getAudioMagnitude(spectrum.getNumBands() / 2);

        for(int i = 0, j = 0;i < MAX_DANCER_ARC_COUNT;i++, j++) {

            final float magX = spectrum.getAudioMagnitude(j);
            final float magY = spectrum.getAudioMagnitude(j * 10);

            Arc arc = arcs.get(i);

            final double oldRotate = arc.getRotate();

            double rx = interpolator.interpolate(arc.getRadiusX(), beat + size + i + magX, interpolationStep);
            double ry = interpolator.interpolate(arc.getRadiusY(), center + size + i + magY, interpolationStep);

            double rotate = oldRotate;

            if(viewModel.getMusicPlayer().getStatus() == PLAYING) {
                rotate = interpolator.interpolate(oldRotate, oldRotate + 0.1 * (beat / Math.max(1, average)) * (0.1 + i), 0.15);
            }

            arc.setRadiusX(rx);
            arc.setRadiusY(ry);
            arc.setRotate(rotate);
        }
    }

    private float getAverage(float[] audioData, int count) {
        return (float) IntStream.range(0, count)
                .mapToDouble(i -> audioData[i] + 60)
                .average().getAsDouble();
    }

    private SpectrumDancerArc onCreateSpectrumDancerBar() {

        SpectrumDancerArc arc = new SpectrumDancerArc();

        final int index = arcs.size();

        final Color color = COLORS.get(index % COLORS.size());

        arc.centerXProperty().bind(songImageFrame.centerXProperty());
        arc.centerYProperty().bind(songImageFrame.centerYProperty());

        arc.setRadiusX(songImageFrame.getRadius() + index);
        arc.setRadiusY(songImageFrame.getRadius() + index);
        arc.translateXProperty().bind(songImageFrame.translateXProperty());
        arc.translateYProperty().bind(songImageFrame.translateYProperty());

        arc.setStartAngle(0);
        arc.setLength(360);
        arc.setStrokeWidth(3);

        arc.setFill(Color.TRANSPARENT);
        arc.setStroke(color);
        arc.setStrokeLineCap(StrokeLineCap.SQUARE);
        arc.setStrokeType(StrokeType.OUTSIDE);
        arc.setStrokeLineJoin(StrokeLineJoin.MITER);
        arc.setStrokeDashOffset(2);

        arc.setEffect(new BoxBlur());

        getChildren().add(arc);

        return arc;
    }

    private static List<Color> createColors() {
        return Arrays.asList(Color.BLUEVIOLET, Color.BLUE, Color.CADETBLUE, Color.DARKGREEN, Color.GREEN, Color.YELLOWGREEN, Color.YELLOW.darker(),
                Color.ORANGE, Color.ORANGERED, Color.RED.darker(), Color.AQUA, Color.BISQUE,
                Color.CYAN, Color.MEDIUMVIOLETRED, Color.KHAKI,Color.GOLD,
                Color.CHOCOLATE, Color.LIGHTSKYBLUE);
    }
}
