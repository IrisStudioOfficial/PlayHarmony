package iris.playharmony.view.util;

import iris.playharmony.util.ImageFactory;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class MultiImageButton extends Button {

    private final Map<Object, ImageView> images;

    public MultiImageButton(String text) {
        super(text);
        images = new HashMap<>();
    }

    public MultiImageButton() {
        images = new HashMap<>();
    }

    public void setImage(Object name) {
        setGraphic(getImageView(name));
    }

    public void addImage(Object name, String imagePath) {
        addImage(name, ImageFactory.loadFromFile(imagePath));
    }

    public void addImage(Object name, Image image) {
        images.put(name, new ImageView(image));
    }

    public ImageView getImageView(Object name) {
        return images.get(name);
    }

    public Image getImage(Object name) {
        return getImageView(name).getImage();
    }

}
