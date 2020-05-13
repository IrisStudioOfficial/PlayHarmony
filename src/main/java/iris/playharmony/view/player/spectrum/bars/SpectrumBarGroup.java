package iris.playharmony.view.player.spectrum.bars;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SpectrumBarGroup {

    private final List<SpectrumBar> bars;
    private final Supplier<SpectrumBar> spectrumBarFactory;

    public SpectrumBarGroup(Supplier<SpectrumBar> spectrumBarFactory) {
        bars = new ArrayList<>(128);
        this.spectrumBarFactory = spectrumBarFactory;
    }

    public SpectrumBar get(int index) {

        SpectrumBar bar;

        if(index >= bars.size()) {

            bar = spectrumBarFactory.get();

            bars.add(bar);

        } else {
            bar = bars.get(index);
        }

        return bar;
    }

    public int size() {
        return bars.size();
    }
}
