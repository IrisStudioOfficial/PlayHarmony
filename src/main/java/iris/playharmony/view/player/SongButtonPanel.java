package iris.playharmony.view.player;

import iris.playharmony.model.player.MusicPlayer;
import iris.playharmony.view.View;
import iris.playharmony.view.util.MultiImageButton;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer.Status;

public class SongButtonPanel extends HBox implements View {

    private static final int SPACING = 20;

    private final MultiImageButton changePlayModeButton;
    private final MultiImageButton backwardsButton;
    private final MultiImageButton playPauseButton;
    private final MultiImageButton forwardButton;

    public SongButtonPanel(MusicPlayerViewModel viewModel) {

        setSpacing(SPACING);

        setAlignment(Pos.CENTER);

        changePlayModeButton = new MultiImageButton("Change Play Mode");

        backwardsButton = new MultiImageButton();
        backwardsButton.addImage("PREV", "C:\\Users\\naits\\Downloads\\back.png");
        backwardsButton.setImage("PREV");

        playPauseButton = new MultiImageButton();
        playPauseButton.addImage("PLAY", "C:\\Users\\naits\\Downloads\\play.png");
        playPauseButton.addImage("PAUSE", "C:\\Users\\naits\\Downloads\\pause.png");
        playPauseButton.setImage("PAUSE");

        playPauseButton.setOnAction(e -> {

            MusicPlayer player = viewModel.getMusicPlayer();

            if(player.getStatus() == Status.PAUSED) {
                player.play();
                playPauseButton.setImage("PAUSE");
            } else {
                player.pause();
                playPauseButton.setImage("PLAY");
            }

        });

        forwardButton = new MultiImageButton();
        forwardButton.addImage("NEXT", "C:\\Users\\naits\\Downloads\\next.png");
        forwardButton.setImage("NEXT");

        // TODO bind buttons with properties

        getChildren().addAll(changePlayModeButton, backwardsButton, playPauseButton, forwardButton);
    }

}
