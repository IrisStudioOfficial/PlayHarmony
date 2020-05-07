package iris.playharmony.model.player;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Map;
import java.util.WeakHashMap;

public class MediaPlayerCache {

    private final Map<Media, MediaPlayer> cache;

    MediaPlayerCache() {
        cache = new WeakHashMap<>();
    }

    public boolean contains(Media media) {
        return cache.containsKey(media);
    }

    public MediaPlayer get(Media media) {
        return cache.get(media);
    }

    public void put(Media media, MediaPlayer player) {
        cache.put(media, player);
    }

    public void remove(Media media) {
        cache.remove(media);
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }
}
