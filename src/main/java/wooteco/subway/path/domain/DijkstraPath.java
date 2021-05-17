package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class DijkstraPath {
    private final DijkstraShortestPath dijkstraShortestPath;

    public DijkstraPath(WeightedGraph graph) {
        this.dijkstraShortestPath = new DijkstraShortestPath(graph.getGraph());
    }

    public Path findShortestPath(Station source, Station target) {
        return new Path(getShortestPath(source, target),
                getShortestDistance(source, target));
    }

    private List<Station> getShortestPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    private int getShortestDistance(Station source, Station target) {
        return (int) dijkstraShortestPath.getPathWeight(source, target);
    }
}
