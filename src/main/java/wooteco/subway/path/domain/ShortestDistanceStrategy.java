package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class ShortestDistanceStrategy implements PathStrategy {
    @Override
    public GraphPath calculateShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                           List<Station> stations, Long sourceId, Long targetId) {

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        Station source = getStation(stations, sourceId);
        Station target = getStation(stations, targetId);

        return dijkstraShortestPath.getPath(source, target);
    }

    private Station getStation(List<Station> stations, Long id) {
        return stations.stream()
                .filter(station -> station.hasSameId(id))
                .findAny()
                .get();
    }
}
