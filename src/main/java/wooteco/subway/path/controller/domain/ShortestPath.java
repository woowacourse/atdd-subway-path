package wooteco.subway.path.controller.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class ShortestPath {
    private final DijkstraShortestPath dijkstraShortestPath;

    public ShortestPath(WeightedMultigraph graph) {
        this.dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    public List<Station> getShortestPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public double getDistance(Station source, Station target) {
        return dijkstraShortestPath.getPathWeight(source, target);
    }
}
