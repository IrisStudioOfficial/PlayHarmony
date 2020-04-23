package iris.playharmony.model;

public class Email {

    private String name;
    private String domain;

    public Email(String name, String domain) {
        this.name = name;
        this.domain = domain;
    }

    public Email(String email) {
        this.name = "";
        this.domain = "";

        for (int i = 0; i < email.length(); i++) {
            if(email.charAt(i) == '@') {
                this.name = email.substring(0, i);
                this.domain = email.substring(i + 1);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return name + '@' + domain;
    }
}
