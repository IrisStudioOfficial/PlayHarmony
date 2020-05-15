package iris.playharmony.controller;

import iris.playharmony.util.OnFinish;
import iris.playharmony.util.Singleton;
import iris.playharmony.view.NavigationView;
import javafx.scene.Parent;

import java.util.Deque;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

import static iris.playharmony.util.TypeUtils.callAnnotatedMethod;

public class NavController implements Iterable<Parent> {

    @Singleton
    private static NavController instance;

    public static NavController get() {
        return instance;
    }

    private final NavigationView navigationView;
    private final Deque<Parent> viewStack;

    public NavController(NavigationView navigationView) {
        this.navigationView = navigationView;
        viewStack = new ConcurrentLinkedDeque<>();
    }

    public void setView(Parent view) {
        clear();
        pushView(view);
    }

    public void pushView(Parent view) {
        viewStack.push(view);
        navigationView.setView(view);
    }

    @SuppressWarnings("unchecked")
    public <T extends Parent> T getCurrentView() {
        return (T) viewStack.peek();
    }

    @SuppressWarnings("unchecked")
    public <T extends Parent> Optional<T> popView() {

        if(viewStack.isEmpty()) {
            return Optional.empty();
        }

        final T currentView = (T) viewStack.pop();

        if(viewStack.isEmpty()) {
            navigationView.removeView();
        } else {
            navigationView.setView(viewStack.peek());
        }

        if(currentView != null) {
            callAnnotatedMethod(currentView, OnFinish.class);
        }

        return Optional.ofNullable(currentView);
    }

    public void clear() {
        viewStack.clear();
    }

    public int count() {
        return viewStack.size();
    }

    @Override
    public Iterator<Parent> iterator() {
        return viewStack.iterator();
    }
}
