package iris.playharmony.view.util;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MultiImageButton extends Button {

    private final Map<String, ImageView> images;

    public MultiImageButton(String text) {
        super(text);
        images = new HashMap<>();
    }

    public MultiImageButton() {
        images = new HashMap<>();
    }

    public void setImage(String name) {
        setGraphic(getImageView(name));
    }

    public void addImage(String name, String imagePath) {
        addImage(name, new Image(new File(imagePath).toURI().toString()));
    }

    public void addImage(String name, Image image) {
        images.put(name, new ImageView(image));
    }

    public ImageView getImageView(String name) {
        return images.get(name);
    }

    public Image getImage(String name) {
        return getImageView(name).getImage();
    }

}
