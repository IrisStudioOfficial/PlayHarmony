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

    private static final String ICONS_PLAYER_BACK = "icons/player/back.png";
    private static final String ICONS_PLAYER_NEXT = "icons/player/next.png";
    private static final String ICONS_PLAYER_PLAY = "icons/player/play.png";
    private static final String ICONS_PLAYER_PAUSE = "icons/player/pause.png";
    private static final String ICONS_PLAYER_SEQUENCE = "icons/player/sequence.png";
    private static final String ICONS_PLAYER_RANDOM = "icons/player/random.png";
    private static final String ICONS_PLAYER_LOOP = "icons/player/loop.png";


    private final Button changePlayModeButton;
    private final Button backwardsButton;
    private final Button playPauseButton;
    private final Button forwardButton;

    public SongButtonPanel(MusicPlayerViewModel viewModel) {

        setSpacing(SPACING);
        setAlignment(Pos.CENTER);

        changePlayModeButton = createChangePlayModeButton(viewModel);

        backwardsButton = ButtonFactory.imageButton(Resources.get(ICONS_PLAYER_BACK));

        playPauseButton = createPlayPauseButton(viewModel);

        forwardButton = ButtonFactory.imageButton(ICONS_PLAYER_NEXT);

        getChildren().addAll(changePlayModeButton, backwardsButton, playPauseButton, forwardButton);
    }

    private Button createPlayPauseButton(MusicPlayerViewModel viewModel) {

        MultiImageButton playPauseButton = new MultiImageButton();

        playPauseButton.addImage(Status.PLAYING, Resources.get(ICONS_PLAYER_PLAY));
        playPauseButton.addImage(Status.PAUSED, Resources.get(ICONS_PLAYER_PAUSE));
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

        changeButton.addImage(SEQUENTIAL, Resources.get(ICONS_PLAYER_SEQUENCE));
        changeButton.addImage(RANDOM, Resources.get(ICONS_PLAYER_RANDOM));
        changeButton.addImage(SELF, Resources.get(ICONS_PLAYER_LOOP));

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
