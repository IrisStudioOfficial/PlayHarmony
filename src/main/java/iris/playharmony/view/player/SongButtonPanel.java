package iris.playharmony.view.player;

import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.view.View;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class SongButtonPanel extends HBox implements View {

    private static final int SPACING = 20;

    private final Button changePlayModeButton;
    private final Button backwardsButton;
    private final Button playPauseButton;
    private final Button forwardButton;

    public SongButtonPanel(MusicPlayerViewModel viewModel) {

        setSpacing(SPACING);

        setAlignment(Pos.CENTER);

        changePlayModeButton = new Button("Change Play Mode");

        backwardsButton = new Button("Previous");

        playPauseButton = new Button("Pause");

        playPauseButton.setOnAction(e -> {

            MusicPlayer player = viewModel.getMusicPlayer();

            if(player.getStatus() == Status.PLAYING) {
                player.pause();
                playPauseButton.setText("Play ");
            } else {
                player.play();
                playPauseButton.setText("Pause");
            }

        });

        forwardButton = new Button("Next");

        // TODO bind buttons with properties

        getChildren().addAll(changePlayModeButton, backwardsButton, playPauseButton, forwardButton);
    }

}
