package wooteco.subway.domain.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import wooteco.subway.domain.station.Station;

import java.util.List;

public class DijkstraShortestPathAlgorithm implements BaseShortestPathAlgorithm {

    private final DijkstraShortestPath dijkstraShortestPath;

    public DijkstraShortestPathAlgorithm(WeightedGraph graph) {
        this.dijkstraShortestPath = new DijkstraShortestPath(graph.getGraph());
    }

    @Override
    public Path findShortestPath(Station source, Station target) {
        return new Path(getShortestPath(source, target),
                getShortestDistance(source, target));
    }

    @Override
    public List<Station> getShortestPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    @Override
    public int getShortestDistance(Station source, Station target) {
        return (int) dijkstraShortestPath.getPathWeight(source, target);
    }
}
