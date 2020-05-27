package iris.playharmony.view;

import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public class NavigationView extends StackPane {

    private final List<NavigationViewChangedListener> listeners;

    public NavigationView() {
        listeners = new ArrayList<>(1);
    }

    public List<NavigationViewChangedListener> getListeners() {
        return listeners;
    }

    public void setView(Parent view) {
        removeView();
        getChildren().add(view);
        notifyListeners(view);
    }

    public void removeView() {
        getChildren().clear();
    }

    private void notifyListeners(Parent view) {
        listeners.forEach(listener -> listener.onViewChanged(view));
    }
}
