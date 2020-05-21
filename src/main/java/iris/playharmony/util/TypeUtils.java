package iris.playharmony.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

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

    public static <T extends Annotation> List<Pair<?, T>> getAnnotatedFields(Object object, Class<T> annotationType) {
        return Stream.of(object.getClass().getDeclaredFields())
                .filter(field -> nonNull(field.getAnnotation(annotationType)))
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        return new Pair<>(field.get(object), field.getAnnotation(annotationType));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(toList());
    }

    private TypeUtils() { }
}
