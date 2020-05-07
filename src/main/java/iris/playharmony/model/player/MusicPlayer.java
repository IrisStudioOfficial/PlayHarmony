package iris.playharmony.model.player;

import javafx.beans.property.*;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import static javafx.scene.media.MediaPlayer.Status.PLAYING;

public class MusicPlayer {

    public static final double DEFAULT_SPECTRUM_INTERVAL = 0.1;
    public static final int DEFAULT_SPECTRUM_NUM_BANDS = 128;
    public static final int DEFAULT_SPECTRUM_THRESHOLD = -60; // dB

    private final DoubleProperty spectrumInterval;
    private final IntegerProperty spectrumNumBands;
    private final IntegerProperty spectrumThreshold;
    private final ObjectProperty<AudioSpectrumListener> spectrumListener;
    private final MediaPlayerCache playerCache;
    private MediaPlayer currentPlayer;
    private boolean loop;

    public MusicPlayer() {

        playerCache = new MediaPlayerCache();

        spectrumInterval = new SimpleDoubleProperty(DEFAULT_SPECTRUM_INTERVAL);
        spectrumNumBands = new SimpleIntegerProperty(DEFAULT_SPECTRUM_NUM_BANDS);
        spectrumThreshold = new SimpleIntegerProperty(DEFAULT_SPECTRUM_THRESHOLD);
        spectrumListener = new SimpleObjectProperty<>();
    }

    public void setSong(Media media) {

        if(getStatus() == PLAYING) {
            stop();
        }

        currentPlayer = playerCache.get(media);

        if(currentPlayer == null) {
            createNewPlayerForMedia(media);
        }
    }

    public void play() {
        if(currentPlayer != null) {
            currentPlayer.play();
        }
    }

    public void play(Media media) {
        setSong(media);
        currentPlayer.play();
    }

    public void pause() {
        if(currentPlayer != null) {
            currentPlayer.pause();
        }
    }

    public void stop() {
        if(currentPlayer != null) {
            currentPlayer.stop();
        }
    }

    public boolean loop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setSecond(double second) {
        currentPlayer.seek(Duration.seconds(second));
    }

    public MediaPlayer.Status getStatus() {
        return currentPlayer == null ? MediaPlayer.Status.UNKNOWN : currentPlayer.getStatus();
    }

    public double getSpectrumInterval() {
        return spectrumInterval.get();
    }

    public void setSpectrumInterval(double interval) {
        spectrumInterval.set(interval);
    }

    public int getSpectrumNumBands() {
        return spectrumNumBands.get();
    }

    public void setSpectrumNumBands(int bands) {
        spectrumNumBands.set(bands);
    }

    public int getSpectrumThreshold() {
        return spectrumThreshold.get();
    }

    public void setSpectrumThreshold(int threshold) {
        spectrumThreshold.set(threshold);
    }

    public AudioSpectrumListener getAudioSpectrumListener() {
        return spectrumListener.get();
    }

    public void setAudioSpectrumListener(AudioSpectrumListener listener) {
        spectrumListener.set(listener);
    }

    public void clearCache() {
        playerCache.clear();
    }

    private void createNewPlayerForMedia(Media media) {

        currentPlayer = new MediaPlayer(media);

        currentPlayer.audioSpectrumIntervalProperty().bind(spectrumInterval);
        currentPlayer.audioSpectrumNumBandsProperty().bind(spectrumNumBands);
        currentPlayer.audioSpectrumThresholdProperty().bind(spectrumThreshold);
        currentPlayer.audioSpectrumListenerProperty().bind(spectrumListener);

        currentPlayer.setOnEndOfMedia(this::onMediaEnd);

        playerCache.put(media, currentPlayer);
    }

    private void onMediaEnd() {
        if(loop) {
            currentPlayer.seek(Duration.ZERO);
        }
    }
}
