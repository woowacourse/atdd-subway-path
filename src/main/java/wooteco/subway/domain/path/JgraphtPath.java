package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;

public class JgraphtPath extends DefaultWeightedEdge {

    private final int extraFare;
    private final double distance;

    public JgraphtPath(int distance, int extraFare) {
        this.distance = distance;
        this.extraFare = extraFare;
    }

    @Override
    protected double getWeight() {
        return distance;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
