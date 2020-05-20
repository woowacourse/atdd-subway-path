package wooteco.subway.admin.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.admin.repository.LineRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class DijkstraPathCalculator implements PathCalculator {

    private final LineRepository lineRepository;

    public DijkstraPathCalculator(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
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

    @Override
    public int calculateTotalDistance(final List<Station> stationsOnPath) {
        return findLineStations(stationsOnPath)
                .mapToInt(LineStation::getDistance)
                .sum();
    }

    @Override
    public int calculateTotalDuration(final List<Station> stationsOnPath) {
        return findLineStations(stationsOnPath)
                .mapToInt(LineStation::getDuration)
                .sum();
    }

    private Stream<LineStation> findLineStations(final List<Station> stationsOnPath) {
        return IntStream.range(0, stationsOnPath.size() - 1)
                .mapToObj(it -> lineRepository.findLineStationByPreStationIdAndStationId(
                        stationsOnPath.get(it).getId(),
                        stationsOnPath.get(it + 1).getId()));
    }
}
