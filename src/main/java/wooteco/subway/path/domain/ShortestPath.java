package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class ShortestPath {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    public ShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap) {
        this.shortestPath = make(subwayMap);
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> make(WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap) {
        return new DijkstraShortestPath<>(subwayMap);
    }

    public List<Station> find(Station source, Station target) {
        return shortestPath.getPath(source, target).getVertexList();
    }

    public int calculate(Station source, Station target) {
        return (int) shortestPath.getPath(source, target).getWeight();
    }
}
