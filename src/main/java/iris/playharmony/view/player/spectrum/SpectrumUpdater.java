package iris.playharmony.view.player.spectrum;

import iris.playharmony.model.player.Spectrum;
import iris.playharmony.view.player.MusicPlayerViewModel;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;

public class SpectrumUpdater extends AnimationTimer {

    private final MusicPlayerViewModel viewModel;
    private final List<SpectrumUpdatable> spectrumUpdatables;
    private float lastTime;

    public SpectrumUpdater(MusicPlayerViewModel viewModel) {
        this.viewModel = viewModel;
        spectrumUpdatables = new ArrayList<>();
    }

    @Override
    public void handle(long now) {

        float deltaTime = now - lastTime;

        lastTime = (float) now;

        Spectrum spectrum = viewModel.getSpectrum();

        if(spectrum.isInitialized()) {
            spectrumUpdatables.forEach(spectrumUpdatable -> spectrumUpdatable.update(spectrum, deltaTime));
        }
    }

    public List<SpectrumUpdatable> getSpectrumUpdatables() {
        return spectrumUpdatables;
    }
}
