package iris.playharmony.model;

public enum Role {

    STUDENT,
    TEACHER,
    ADMIN;

    public static Role getRoleFrom(String role) {
        return valueOf(role.toUpperCase());
    }
}
