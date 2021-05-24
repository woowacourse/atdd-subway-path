package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    private final GraphPath<Station, DefaultWeightedEdge> graphPath;

    public SubwayPath(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        this.graphPath = graphPath;
    }

    public List<Station> getShortestPath() {
        return graphPath.getVertexList();
    }

    public int getTotalDistance() {
        return (int) graphPath.getWeight();
    }
}
