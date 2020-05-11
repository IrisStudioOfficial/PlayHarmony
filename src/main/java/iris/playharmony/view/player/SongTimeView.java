package iris.playharmony.view.player;

import iris.playharmony.view.View;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

public class SongTimeView extends HBox implements View {

    private static final int SPACING = 20;

    private final Label currentTimeLabel;
    private final Slider timeSlider;
    private final Label totalDurationLabel;

    public SongTimeView(MusicPlayerViewModel viewModel) {
        setSpacing(SPACING);
        currentTimeLabel = createTimeLabel(viewModel);
        timeSlider = createTimeSlider(viewModel);
        totalDurationLabel = createDurationLabel(viewModel);
    }

    private Label createTimeLabel(MusicPlayerViewModel viewModel) {

        Label timeLabel = new Label();

        viewModel.getMusicPlayer().currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            timeLabel.setText(newValue.toString());
        });

        add(timeLabel);

        return timeLabel;
    }

    private Slider createTimeSlider(MusicPlayerViewModel viewModel) {

        Slider slider = new Slider();

        viewModel.getMusicPlayer().currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            slider.setValue(newValue.toSeconds());
        });

        add(slider);

        return slider;
    }

    private Label createDurationLabel(MusicPlayerViewModel viewModel) {

        Label durationLabel = new Label();

        viewModel.getMusicPlayer().totalDurationProperty().addListener((observable, oldValue, newValue) -> {
            durationLabel.setText(newValue.toString());
        });

        add(durationLabel);

        return durationLabel;
    }

}
