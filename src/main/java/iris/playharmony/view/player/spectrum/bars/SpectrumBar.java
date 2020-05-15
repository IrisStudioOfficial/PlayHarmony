package iris.playharmony.view.player.spectrum.bars;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SpectrumBar extends Rectangle {

    private static final Color FALLING_COLOR = Color.ORANGE.darker();
    private static final Color RISING_COLOR = Color.ORANGE;

    public SpectrumBar() {
    }

    public void setRising() {
        setFill(RISING_COLOR);
    }

    public void setFalling() {
        setFill(FALLING_COLOR);
    }
}
