package iris.playharmony.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
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

    public static void callAnnotatedMethod(Object object, Class<? extends Annotation> annotation) {
        if(object == null) {
            return;
        }

        Stream.of(object.getClass().getMethods())
                .filter(m -> nonNull(m.getAnnotation(annotation)))
                .findAny()
                .ifPresent(method -> {
                    try {
                        method.invoke(object);
                    } catch (Exception e) {
                        Logger.getGlobal().log(Level.SEVERE, "Cannot access field " + method.getName(), e);
                    }
                });
    }

    private TypeUtils() { }
}
