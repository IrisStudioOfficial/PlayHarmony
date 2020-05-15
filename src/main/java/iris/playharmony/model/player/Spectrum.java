package iris.playharmony.model.player;

import iris.playharmony.util.GaussianSmooth;
import javafx.animation.Interpolator;
import javafx.scene.media.AudioSpectrumListener;

import java.util.Arrays;


public class Spectrum implements AudioSpectrumListener {

    private static final double DEFAULT_SPECTRUM_INTERVAL = 0.01;
    private static final int DEFAULT_SPECTRUM_NUM_BANDS = 800;
    private static final int DEFAULT_SPECTRUM_THRESHOLD = -60; // dB

    // The audio properties
    private final float[] audioData;
    private final int threshold;
    private final double interval;
    private boolean initialized;
    // The interpolator
    private final Interpolator interpolator;

    public Spectrum(Interpolator interpolator) {
        this(interpolator, DEFAULT_SPECTRUM_NUM_BANDS, DEFAULT_SPECTRUM_THRESHOLD, DEFAULT_SPECTRUM_INTERVAL);
    }

    public Spectrum(Interpolator interpolator, int numBands, int threshold, double interval) {
        audioData = new float[numBands];
        this.threshold = threshold;
        this.interval = interval;
        this.interpolator = interpolator;
    }

    @Override
    public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        GaussianSmooth.gaussianSmooth(magnitudes, audioData, magnitudes.length);
        for(int i = 0;i < 3;i++) {
            GaussianSmooth.gaussianSmooth(audioData, audioData, magnitudes.length);
        }
        initialized = true;
    }

    public int getNumBands() {
        return audioData.length;
    }

    public float[] getAudioData() {
        return audioData;
    }

    public float getAudioMagnitude(int i) {
        return audioData[i] - threshold;
    }

    public int getThreshold() {
        return threshold;
    }

    public double getInterval() {
        return interval;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
