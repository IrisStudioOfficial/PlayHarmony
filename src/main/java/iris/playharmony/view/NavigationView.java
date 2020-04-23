package iris.playharmony.view;

import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class NavigationView extends StackPane {

    public NavigationView() {

    }

    public void setView(Parent view) {
        removeView();
        getChildren().add(view);
    }

    public void removeView() {
        getChildren().clear();
    }
}
