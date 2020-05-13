package iris.playharmony.session;

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
    }

    public User currentUser() {
        return user;
    }

    public void setCurrentUser(User user) {
        this.user = user;
    }
}
