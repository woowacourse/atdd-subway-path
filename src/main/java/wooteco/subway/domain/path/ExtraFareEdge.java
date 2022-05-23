package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;

import wooteco.subway.domain.property.fare.Fare;

public class ExtraFareEdge extends DefaultWeightedEdge {

    private final Fare extraFare;

    public ExtraFareEdge(Fare extraFare) {
        this.extraFare = extraFare;
    }

    public Fare getExtraFare() {
        return extraFare;
    }
}
