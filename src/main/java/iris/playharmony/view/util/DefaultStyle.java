package iris.playharmony.view.util;

import javafx.scene.text.Font;

public class DefaultStyle {

    public static Style title() {
        return new Style().font(new Font("Arial", 18));
    }

    public static Style label() {
        return new Style().font(new Font("Arial", 14));
    }
}
