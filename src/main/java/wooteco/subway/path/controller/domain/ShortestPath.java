package wooteco.subway.path.controller.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class ShortestPath {
    private final DijkstraShortestPath dijkstraShortestPath;

    public ShortestPath(WeightedGraph graph) {
        this.dijkstraShortestPath = new DijkstraShortestPath(graph.getGraph());
    }

    public List<Station> getShortestPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public int getShortestDistance(Station source, Station target) {
        return (int) dijkstraShortestPath.getPathWeight(source, target);
    }
}
