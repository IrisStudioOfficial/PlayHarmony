package iris.playharmony.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

public final class TypeUtils {

    public static void initSingleton(Class<?> clazz, Object value) {
        Stream.of(clazz.getDeclaredFields())
                .filter(f -> nonNull(f.getAnnotation(Singleton.class)))
                .findAny()
                .ifPresent(field -> {
                    field.setAccessible(true);
                    try {
                        field.set(null, value);
                    } catch (IllegalAccessException e) {
                        Logger.getGlobal().log(Level.SEVERE, "Cannot access field " + field.getName(), e);
                    }
                });
    }

    private TypeUtils() { }
}
