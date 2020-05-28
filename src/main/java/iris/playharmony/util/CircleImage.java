package iris.playharmony.util;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class CircleImage extends Circle {

    private static final float DEFAULT_RADIUS = 32.0f;

    public CircleImage() {
        super(DEFAULT_RADIUS);
        setEffect(new DropShadow());
    }

    public CircleImage(String url) {
        this();
        setImage(new Image(url));
    }

    public CircleImage(Image image) {
        this();
        setImage(image);
    }

    public Image getImage() {
        if(getFill() == null) {
            return null;
        }
        return ((ImagePattern) getFill()).getImage();
    }

    public void setImage(Image image) {
        if(image == null) {
            setFill(null);
        } else {
            setFill(new ImagePattern(image));
        }
    }
}
