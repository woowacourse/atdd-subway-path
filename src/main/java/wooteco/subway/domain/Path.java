package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Path {

    public static final int BASIC_FARE = 1250;
    private final GraphPath<Station, DefaultWeightedEdge> graphPath;

    public Path(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        this.graphPath = graphPath;
    }

    public List<Station> findStationsOnPath() {
        return graphPath.getVertexList();
    }

    public int calculateShortestDistance() {
        return (int) graphPath.getWeight();
    }
}
