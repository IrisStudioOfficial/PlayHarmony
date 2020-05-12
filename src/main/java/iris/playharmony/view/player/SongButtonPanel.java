package iris.playharmony.view.player;

import iris.playharmony.model.SongPlayMode;
import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.util.Resources;
import iris.playharmony.view.util.ButtonFactory;
import iris.playharmony.view.util.MultiImageButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer.Status;

import static iris.playharmony.model.SongPlayMode.*;

public class SongButtonPanel extends HBox {

    private static final int SPACING = 20;

    private final Button changePlayModeButton;
    private final Button backwardsButton;
    private final Button playPauseButton;
    private final Button forwardButton;

    public SongButtonPanel(MusicPlayerViewModel viewModel) {

        setSpacing(SPACING);
        setAlignment(Pos.CENTER);

        changePlayModeButton = createChangePlayModeButton(viewModel);

        backwardsButton = ButtonFactory.imageButton(Resources.get("icons/player/back.png"));

        playPauseButton = createPlayPauseButton(viewModel);

        forwardButton = ButtonFactory.imageButton("icons/player/next.png");

        getChildren().addAll(changePlayModeButton, backwardsButton, playPauseButton, forwardButton);
    }

    private Button createPlayPauseButton(MusicPlayerViewModel viewModel) {

        MultiImageButton playPauseButton = new MultiImageButton();

        playPauseButton.addImage(Status.PLAYING, Resources.get("icons/player/play.png"));
        playPauseButton.addImage(Status.PAUSED, Resources.get("icons/player/pause.png"));
        playPauseButton.setImage(Status.PAUSED);

        playPauseButton.setOnAction(e -> {

            MusicPlayer player = viewModel.getMusicPlayer();

            Status playerStatus = player.getStatus();

            playPauseButton.setImage(playerStatus);

            if(playerStatus == Status.PAUSED) {
                player.play();
            } else {
                player.pause();
            }
        });

        return playPauseButton;
    }

    private MultiImageButton createChangePlayModeButton(MusicPlayerViewModel viewModel) {

        MultiImageButton changeButton = new MultiImageButton();

        changeButton.addImage(SEQUENTIAL, Resources.get("icons/player/sequence.png"));
        changeButton.addImage(RANDOM, Resources.get("icons/player/random.png"));
        changeButton.addImage(SELF, Resources.get("icons/player/loop.png"));

        changeButton.setImage(SEQUENTIAL);

        viewModel.songPlayModeProperty().addListener((observable, oldValue, newValue) -> changeButton.setImage(newValue));

        changeButton.setOnAction(e -> {

            final int playModeIndex = viewModel.songPlayModeProperty().get().ordinal();

            SongPlayMode[] playModes = SongPlayMode.values();

            SongPlayMode nextMode = playModes[(playModeIndex + 1) % playModes.length];

            viewModel.songPlayModeProperty().set(nextMode);
        });

        return changeButton;
    }

}
