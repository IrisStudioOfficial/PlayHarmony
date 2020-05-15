package iris.playharmony.util;

import java.io.File;
import java.net.URISyntaxException;

public class Resources {

    public static String get(String resourceName) {

        if(!resourceName.startsWith("/")) {
            resourceName = "/" + resourceName;
        }

        try {
            return new File(Resources.class.getResource(resourceName).toURI().getPath()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
