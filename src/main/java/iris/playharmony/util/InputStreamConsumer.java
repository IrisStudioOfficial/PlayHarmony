package iris.playharmony.util;

import java.io.InputStream;
import java.util.function.Consumer;

public interface InputStreamConsumer extends Consumer<InputStream> {

    @Override
    default void accept(InputStream inputStream) {
        try {
            consume(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void consume(InputStream inputStream) throws Exception;
}
