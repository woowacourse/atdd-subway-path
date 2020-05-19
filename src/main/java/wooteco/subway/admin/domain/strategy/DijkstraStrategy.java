package wooteco.subway.admin.domain.strategy;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.exception.NotConnectEdgeException;

public class DijkstraStrategy implements AlgorithmStrategy {
    @Override
    public GraphPath<Station, Edge> getPath(final Map<Long, Station> stations,
        final List<Line> lines, final PathRequest pathRequest) {

        final Station startStation = getStationWithValidation(stations, pathRequest.getSource());
        final Station endStation = getStationWithValidation(stations, pathRequest.getTarget());

        DijkstraShortestPath<Station, Edge> dijkstraShortestPath =
            new DijkstraShortestPath<>(makeGraph(stations, lines, pathRequest.getType()));

        final GraphPath<Station, Edge> path = dijkstraShortestPath.getPath(startStation,
            endStation);

        validateConnection(startStation, endStation, path);

        return path;
    }

    private WeightedMultigraph<Station, Edge> makeGraph(Map<Long, Station> stations,
        List<Line> lines,
        PathType pathType) {
        WeightedMultigraph<Station, Edge> graph
            = new WeightedMultigraph<>(Edge.class);

        stations.values()
            .forEach(graph::addVertex);

        lines.stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .filter(LineStation::isEdge)
            .forEach(lineStation -> {
                Station preStation = getStationWithValidation(stations,
                    lineStation.getPreStationId());
                Station currentStation = getStationWithValidation(stations,
                    lineStation.getStationId());
                Edge edge = lineStation.toEdge();

                graph.addEdge(preStation, currentStation, edge);
                graph.setEdgeWeight(edge, pathType.getValue(lineStation));
            });
        return graph;
    }

    private void validateConnection(final Station startStation, final Station endStation,
        final GraphPath<Station, Edge> path) {
        if (Objects.isNull(path)) {
            throw new NotConnectEdgeException(startStation, endStation);
        }
    }

    private Station getStationWithValidation(Map<Long, Station> stations, Long stationId) {
        if (!stations.containsKey(stationId)) {
            throw new NoSuchElementException("해당 역은 등록되어있지 않은 역입니다.");
        }

        return stations.get(stationId);
    }
}
