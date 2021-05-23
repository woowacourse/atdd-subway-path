package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SubwayMap {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> currentSubwayMap;

    public SubwayMap(WeightedMultigraph<Station, DefaultWeightedEdge> currentSubwayMap) {
        this.currentSubwayMap = currentSubwayMap;
    }

    public Map<List<Station>, Double> calculateShortestPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath =
                new DijkstraShortestPath<>(currentSubwayMap);
        final GraphPath<Station, DefaultWeightedEdge> shortestGraphPath = dijkstraShortestPath.getPath(source, target);
        final List<Station> shortestPathStations = shortestGraphPath.getVertexList();
        final double shortestDistance = shortestGraphPath.getWeight();
        return Collections.singletonMap(shortestPathStations, shortestDistance);
    }
}
