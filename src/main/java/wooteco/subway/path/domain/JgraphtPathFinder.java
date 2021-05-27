package wooteco.subway.path.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.station.domain.Station;

public class JgraphtPathFinder implements ShortestPathFinder {

    private final DijkstraShortestPath dijkstraShortestPath;

    public JgraphtPathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    @Override
    public List<Station> findShortestPath(Station from, Station to) {
        return dijkstraShortestPath.getPath(from, to).getVertexList();
    }

    @Override
    public int findShortestDistance(Station from, Station to) {
        return (int) dijkstraShortestPath.getPath(from, to).getWeight();
    }
}
