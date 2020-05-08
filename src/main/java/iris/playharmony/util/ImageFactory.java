package iris.playharmony.util;

import javafx.scene.image.Image;

import java.io.File;

public class ImageFactory {

    public static Image loadFromFile(File file) {
        return new Image(file.toString());
    }
}
