package iris.playharmony.model.player;

import javafx.beans.property.*;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

import static javafx.scene.media.MediaPlayer.Status.PLAYING;

public class MusicPlayer {

    // Spectrum
    private final DoubleProperty spectrumIntervalProperty;
    private final IntegerProperty spectrumNumBandsProperty;
    private final IntegerProperty spectrumThresholdProperty;
    private final ObjectProperty<AudioSpectrumListener> spectrumListenerProperty;

    // MediaPlayer
    private final MediaPlayerCache playerCache;
    private final ObjectProperty<MediaPlayer> currentPlayerProperty;
    private final ObjectProperty<Duration> currentTimeProperty;
    private final ObjectProperty<Duration> totalDurationProperty;
    private final ObjectProperty<Status> statusProperty;

    // Other properties
    private final BooleanProperty loopProperty;

    public MusicPlayer() {

        playerCache = new MediaPlayerCache();

        spectrumIntervalProperty = new SimpleDoubleProperty();
        spectrumNumBandsProperty = new SimpleIntegerProperty();
        spectrumThresholdProperty = new SimpleIntegerProperty();
        spectrumListenerProperty = new SimpleObjectProperty<>();

        currentPlayerProperty = new SimpleObjectProperty<>();
        currentTimeProperty = new SimpleObjectProperty<>();
        totalDurationProperty = new SimpleObjectProperty<>();
        statusProperty = new SimpleObjectProperty<>();

        loopProperty = new SimpleBooleanProperty();
    }

    public void setSong(Media media) {

        if(getStatus() == PLAYING) {
            stop();
        }

        MediaPlayer currentPlayer = playerCache.get(media);

        if(currentPlayer == null) {
            currentPlayer = createNewPlayerForMedia(media);
        }

        updateMediaPlayerPropertyBindings(currentPlayer);
    }

    public void play() {
        if(getCurrentPlayer() != null) {
            getCurrentPlayer().play();
        }
    }

    public void play(Media media) {
        setSong(media);
        getCurrentPlayer().play();
    }

    public void pause() {
        if(getCurrentPlayer() != null) {
            getCurrentPlayer().pause();
        }
    }

    public void stop() {
        if(getCurrentPlayer() != null) {
            getCurrentPlayer().stop();
        }
    }

    public double getSpectrumInterval() {
        return spectrumIntervalProperty.get();
    }

    public DoubleProperty spectrumIntervalProperty() {
        return spectrumIntervalProperty;
    }

    public void setSpectrumInterval(double spectrumIntervalProperty) {
        this.spectrumIntervalProperty.set(spectrumIntervalProperty);
    }

    public int getSpectrumNumBands() {
        return spectrumNumBandsProperty.get();
    }

    public IntegerProperty spectrumNumBandsProperty() {
        return spectrumNumBandsProperty;
    }

    public void setSpectrumNumBands(int spectrumNumBandsProperty) {
        this.spectrumNumBandsProperty.set(spectrumNumBandsProperty);
    }

    public int getSpectrumThreshold() {
        return spectrumThresholdProperty.get();
    }

    public IntegerProperty spectrumThreshold() {
        return spectrumThresholdProperty;
    }

    public void setSpectrumThreshold(int spectrumThresholdProperty) {
        this.spectrumThresholdProperty.set(spectrumThresholdProperty);
    }

    public AudioSpectrumListener getSpectrumListener() {
        return spectrumListenerProperty.get();
    }

    public ObjectProperty<AudioSpectrumListener> spectrumListenerProperty() {
        return spectrumListenerProperty;
    }

    public void setSpectrumListener(AudioSpectrumListener spectrumListenerProperty) {
        this.spectrumListenerProperty.set(spectrumListenerProperty);
    }

    public MediaPlayer getCurrentPlayer() {
        return currentPlayerProperty.get();
    }

    public ReadOnlyObjectProperty<MediaPlayer> currentPlayerProperty() {
        return currentPlayerProperty;
    }

    public Duration getCurrentTime() {
        return currentTimeProperty.get();
    }

    public ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        return currentTimeProperty;
    }

    public void setCurrentTime(Duration time) {
        if(getCurrentPlayer() != null) {
            getCurrentPlayer().seek(time);
        }
    }

    public Duration getTotalDuration() {
        return totalDurationProperty.get();
    }

    public ReadOnlyObjectProperty<Duration> totalDurationProperty() {
        return totalDurationProperty;
    }

    public Status getStatus() {
        return statusProperty.get();
    }

    public ReadOnlyObjectProperty<Status> statusProperty() {
        return statusProperty;
    }

    public void setStatus(Status statusProperty) {
        this.statusProperty.set(statusProperty);
    }

    public boolean isLoop() {
        return loopProperty.get();
    }

    public BooleanProperty loopProperty() {
        return loopProperty;
    }

    public void setLoopProperty(boolean loopProperty) {
        this.loopProperty.set(loopProperty);
    }

    public void clearCache() {
        playerCache.clear();
    }

    private void updateMediaPlayerPropertyBindings(MediaPlayer currentPlayer) {

        currentPlayerProperty.set(currentPlayer);

        currentTimeProperty.bind(currentPlayer.currentTimeProperty());
        totalDurationProperty.bind(currentPlayer.totalDurationProperty());
        statusProperty.bind(currentPlayer.statusProperty());
    }

    private MediaPlayer createNewPlayerForMedia(Media media) {

        MediaPlayer currentPlayer = new MediaPlayer(media);

        currentPlayer.audioSpectrumIntervalProperty().bind(spectrumIntervalProperty);
        currentPlayer.audioSpectrumNumBandsProperty().bind(spectrumNumBandsProperty);
        currentPlayer.audioSpectrumThresholdProperty().bind(spectrumThresholdProperty);
        currentPlayer.audioSpectrumListenerProperty().bind(spectrumListenerProperty);

        currentPlayer.setOnEndOfMedia(this::onMediaEnd);

        playerCache.put(media, currentPlayer);

        return currentPlayer;
    }

    private void onMediaEnd() {
        if(loopProperty.get()) {
            getCurrentPlayer().seek(Duration.ZERO);
        }
    }
}
