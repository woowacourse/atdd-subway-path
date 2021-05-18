package wooteco.subway.path.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.station.domain.Station;

public class ShortestPathMap {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> path;

    public ShortestPathMap(WeightedMultigraph<Station, DefaultWeightedEdge> distanceMap) {
        this(new DijkstraShortestPath<>(distanceMap));
    }

    public ShortestPathMap(DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath) {
        this.path = shortestPath;
    }

    public List<Station> path(Station source, Station target) {
        return path.getPath(source, target).getVertexList();
    }

    public int distance(Station source, Station target) {
        return (int) path.getPathWeight(source, target);
    }
}
