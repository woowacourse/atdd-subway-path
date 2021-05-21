package wooteco.subway.path.infrastructure;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.line.domain.Section;
import wooteco.subway.path.dto.Path;
import wooteco.subway.station.domain.Station;


public class ShortestPathWithDijkstra extends ShortestPathGraph {
    private final DijkstraShortestPath dijkstraShortestPath;

    public ShortestPathWithDijkstra(List<Section> sections) {
        super(new WeightedMultigraph<>(DefaultWeightedEdge.class), sections);
        this.dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    @Override
    public List<Station> getStations(Station source, Station target) {
        GraphPath path = this.dijkstraShortestPath.getPath(source, target);
        return Objects.requireNonNull(path.getVertexList());
    }

    @Override
    public int getDistance(Station source, Station target) {
        GraphPath path = this.dijkstraShortestPath.getPath(source, target);
        return (int) path.getWeight();
    }
}
