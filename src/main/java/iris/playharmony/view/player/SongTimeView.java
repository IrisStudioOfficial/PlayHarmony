package iris.playharmony.view.player;

import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.view.View;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicBoolean;

public class SongTimeView extends HBox implements View {

    private static final int SPACING = 20;

    private static final String SONG_TIME_FORMAT = "%02d:%02d";

    private final Label currentTimeLabel;
    private final Slider timeSlider;
    private final Label totalDurationLabel;

    public SongTimeView(MusicPlayerViewModel viewModel) {

        setSpacing(SPACING);
        setAlignment(Pos.CENTER);

        currentTimeLabel = createTimeLabel(viewModel);
        timeSlider = createTimeSlider(viewModel);
        totalDurationLabel = createDurationLabel(viewModel);
    }

    private Label createTimeLabel(MusicPlayerViewModel viewModel) {

        Label timeLabel = new Label();

        viewModel.getMusicPlayer().currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            timeLabel.setText(String.format(SONG_TIME_FORMAT, (int)newValue.toMinutes() % 60, (int)newValue.toSeconds() % 60));
        });

        add(timeLabel);

        return timeLabel;
    }

    private Slider createTimeSlider(MusicPlayerViewModel viewModel) {

        Slider slider = new Slider();

        MusicPlayer musicPlayer = viewModel.getMusicPlayer();

        slider.setMin(0.0);

        AtomicBoolean changedByMusicPlayer = new AtomicBoolean(false);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(!changedByMusicPlayer.get()) {
                musicPlayer.setCurrentTime(Duration.seconds(newValue.doubleValue()));
            }
        });

        musicPlayer.totalDurationProperty().addListener((observable, oldValue, newValue) -> {
            slider.setMax(newValue.toSeconds());
        });

        musicPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            changedByMusicPlayer.set(true);
            slider.setValue(newValue.toSeconds());
            changedByMusicPlayer.set(false);
        });

        slider.setMinWidth(360);

        add(slider);

        return slider;
    }

    private Label createDurationLabel(MusicPlayerViewModel viewModel) {

        Label durationLabel = new Label();

        viewModel.getMusicPlayer().totalDurationProperty().addListener((observable, oldValue, newValue) -> {
            durationLabel.setText(String.format(SONG_TIME_FORMAT, (int)newValue.toMinutes() % 60, (int)newValue.toSeconds() % 60));
        });

        add(durationLabel);

        return durationLabel;
    }

}
