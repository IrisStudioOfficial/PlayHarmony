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

    }

    public User currentUser() {
        return user;
    }

    public void setCurrentUser(User user) {
        this.user = user;
    }
}
