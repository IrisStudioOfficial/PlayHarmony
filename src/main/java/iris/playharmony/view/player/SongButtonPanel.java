package iris.playharmony.view.player;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class SongButtonPanel extends HBox {

    private static final int SPACING = 20;

    private final Button changePlayModeButton;
    private final Button backwardsButton;
    private final Button playPauseButton;
    private final Button forwardButton;

    public SongButtonPanel(MusicPlayerViewModel viewModel) {

        setSpacing(SPACING);

        setAlignment(Pos.CENTER);

        changePlayModeButton = new Button("Change");
        backwardsButton = new Button("<=");
        playPauseButton = new Button("||");
        forwardButton = new Button("=>");

        // TODO bind buttons with properties

        getChildren().addAll(changePlayModeButton, backwardsButton, playPauseButton, forwardButton);
    }

}
