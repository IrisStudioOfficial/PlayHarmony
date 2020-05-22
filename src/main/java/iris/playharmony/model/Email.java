package iris.playharmony.model;

import iris.playharmony.util.Pair;

public class Email {

    public static boolean check(String text) {
        Pair<String, String> emailAttributes = getEmailAttributesFromText(text);
        return check(emailAttributes.getKey(), emailAttributes.getValue());
    }

    public static boolean check(String name, String domain) {
        if(name == null || name.isEmpty() || domain == null || domain.isEmpty()) {
            return false;
        }
        return true;
    }

    private static Pair<String, String> getEmailAttributesFromText(String text) {

        String name = null;
        String domain = null;

        for (int i = 0; i < text.length(); i++) {
            if(text.charAt(i) == '@') {
                name = text.substring(0, i);
                domain = text.substring(i + 1);
                break;
            }
        }

        return new Pair<>(name, domain);
    }

    private String name;
    private String domain;

    public Email(String name, String domain) {
        this.name = name;
        this.domain = domain;
    }

    public Email(String text) {

        Pair<String, String> emailAttributes = getEmailAttributesFromText(text);

        if(!check(emailAttributes.getKey(), emailAttributes.getValue())) {
            throw new IllegalArgumentException();
        }

        this.name = emailAttributes.getKey();
        this.domain = emailAttributes.getValue();
    }

    @Override
    public String toString() {
        return name + '@' + domain;
    }
}
