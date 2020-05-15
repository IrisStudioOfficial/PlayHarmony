package iris.playharmony.view.player.spectrum.dancer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SpectrumDancerArcGroup {

    private final List<SpectrumDancerArc> arcs;
    private final Supplier<SpectrumDancerArc> spectrumDancerArcFactory;

    public SpectrumDancerArcGroup(Supplier<SpectrumDancerArc> spectrumDancerArcFactory) {
        arcs = new ArrayList<>(24);
        this.spectrumDancerArcFactory = spectrumDancerArcFactory;
    }

    public SpectrumDancerArc get(int index) {

        SpectrumDancerArc arc;

        if(index >= arcs.size()) {

            arc = spectrumDancerArcFactory.get();

            arcs.add(arc);

        } else {
            arc = arcs.get(index);
        }

        return arc;
    }

    public int size() {
        return arcs.size();
    }
}
