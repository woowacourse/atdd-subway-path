package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class ExtraFareSubwayEdge extends DefaultWeightedEdge {

    private final int extraFare;

    public ExtraFareSubwayEdge(int extraFare) {
        this.extraFare = extraFare;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
