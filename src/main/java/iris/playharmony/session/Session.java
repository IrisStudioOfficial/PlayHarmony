package iris.playharmony.session;

import iris.playharmony.controller.db.DatabaseController;
import iris.playharmony.model.User;

public class Session {

    private static Session session;

    public static Session getSession() {

        if(session == null) {
            session = new Session();
        }

        return session;
    }

    private User user;

    private Session() {
        this.user = DatabaseController.get().getUsers().stream()
                .filter(user -> user.getName().equals("test"))
                .findFirst().get();
    }

    public User currentUser() {
        user = DatabaseController.get().getUsers().stream()
                .filter(user -> user.getName().equals(this.user.getName()))
                .findFirst().get();
        return user;
    }

    public void setCurrentUser(User user) {
        this.user = user;
    }
}
