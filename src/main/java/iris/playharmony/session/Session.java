package iris.playharmony.session;

import iris.playharmony.controller.DatabaseController;
import iris.playharmony.model.User;

public class Session {
    private User user;
    private static Session session;

    public static Session getSession() {
        if(session == null)
            session = new Session();
        return session;
    }

    private Session() {
        this.user = new DatabaseController().getUsers().stream()
                .filter(user -> user.getName().equals("test"))
                .findFirst().get();
    }

    public User currentUser() {
        user = new DatabaseController().getUsers().stream()
                .filter(user -> user.getName().equals(this.user.getName()))
                .findFirst().get();
        return user;
    }

    public void setCurrentUser(User user) {
        this.user = user;
    }
}
