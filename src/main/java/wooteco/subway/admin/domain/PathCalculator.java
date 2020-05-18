package wooteco.subway.admin.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class PathCalculator {

    public List<Long> findShortestPath(final Station sourceStation, final Station targetStation, final SearchType searchType,
                                       final List<Station> allStations, final List<LineStation> allLineStations) {
        DijkstraShortestPath dijkstraShortestPath = findDijkstraPath(
                allStations, allLineStations, searchType);
        return dijkstraShortestPath.getPath(
                sourceStation.getId(), targetStation.getId()).getVertexList();
    }

    private DijkstraShortestPath findDijkstraPath(final List<Station> allStations,
                                                  final List<LineStation> allLineStations,
                                                  final SearchType searchType) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        graph.addVertex(0L);
        allStations.forEach(station -> graph.addVertex(station.getId()));
        allLineStations.forEach(it ->
                graph.setEdgeWeight(graph.addEdge(getVertexOfPreStationId(it), it.getStationId()),
                        searchType.isDistance() ? it.getDistance() : it.getDuration())
        );

        return new DijkstraShortestPath(graph);
    }

    private Long getVertexOfPreStationId(final LineStation lineStation) {
        if (Objects.isNull(lineStation.getPreStationId())) {
            return 0L;
        }
        return lineStation.getPreStationId();
    }
}
