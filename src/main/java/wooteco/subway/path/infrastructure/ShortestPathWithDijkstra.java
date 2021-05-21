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
    private final GraphPath dijkstraShortestPath;

    public ShortestPathWithDijkstra(List<Section> sections, Station source, Station target) {
        super(new WeightedMultigraph<>(DefaultWeightedEdge.class), sections);
        this.dijkstraShortestPath = new DijkstraShortestPath(graph).getPath(source, target);
    }

    @Override
    public List<Station> getStations() {
        return Objects.requireNonNull(dijkstraShortestPath.getVertexList());
    }

    @Override
    public int getDistance() {
        return (int) dijkstraShortestPath.getWeight();
    }
}
