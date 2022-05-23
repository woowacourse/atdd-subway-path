package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class PathEdge extends DefaultWeightedEdge {

    private final int distance;
    private final int extraFare;

    public PathEdge(final int distance, final int extraFare) {
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public static PathEdge from(final Section section) {
        return new PathEdge(section.getDistance(), section.getExtraFare());
    }

    public int getExtraFare() {
        return extraFare;
    }

    @Override
    public double getWeight() {
        return distance;
    }
}
