package iris.playharmony.util;

import javafx.scene.image.Image;

import java.io.File;

public class ImageFactory {

    public static Image loadFromFile(String file) {
        return new Image(new File(file).toURI().toString());
    }
}
