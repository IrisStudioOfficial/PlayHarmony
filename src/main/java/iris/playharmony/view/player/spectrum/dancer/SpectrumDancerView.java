package iris.playharmony.view.player.spectrum.dancer;

import iris.playharmony.model.player.Spectrum;
import iris.playharmony.view.player.MusicPlayerViewModel;
import iris.playharmony.view.player.spectrum.SpectrumUpdatable;
import javafx.animation.Interpolator;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.effect.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
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

        this.viewModel = viewModel;

        songImageFrame = new Circle();

        songImageFrame.setTranslateZ(-0.1);

        songImageFrame.setStroke(Color.TRANSPARENT);

        parentLayoutBounds.addListener((observable, oldValue, newValue) -> {
            songImageFrame.setTranslateX(newValue.getMaxX() / 2.0);
            songImageFrame.setTranslateY(newValue.getMinY() + songImageFrame.getRadius() * 2);
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

        songImageFrame.setRadius(beat * 0.2 + MIN_RADIUS);

        Interpolator interpolator = Interpolator.EASE_BOTH;

        final double interpolationStep = 0.8;

        final float minRadius = (float) songImageFrame.getRadius();

        float size = beat + minRadius;

        /*

        if(beat >= 50) {
            size *= 1.5f;
        }

        if(beat >= 70) {
            size *= 2.0;
        }

         */

        for(int i = 0, j = 0;i < MAX_DANCER_ARC_COUNT;i++, j+=2) {

            final float magX = spectrum.getAudioMagnitude(j);
            final float magY = spectrum.getAudioMagnitude(j + 1);

            Arc arc = arcs.get(i);

            final double oldRotate = arc.getRotate();

            final double rx = interpolator.interpolate(arc.getRadiusX(), size + i + magX, interpolationStep);
            final double ry = interpolator.interpolate(arc.getRadiusY(), size + i + magY, interpolationStep);

            double rotate = oldRotate;

            if(viewModel.getMusicPlayer().getStatus() == PLAYING) {
                rotate = interpolator.interpolate(oldRotate, oldRotate + average + (i + 1), 0.1);
            }

            arc.setRadiusX(Math.max(rx, minRadius + 1));
            arc.setRadiusY(Math.max(ry, minRadius + 1));
            arc.setRotate(rotate);
        }
    }

    private float getAverage(float[] audioData, int count) {
        return (float) IntStream.range(0, count)
                .mapToDouble(i -> audioData[i])
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

        // arc.setEffect(new DropShadow());

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
