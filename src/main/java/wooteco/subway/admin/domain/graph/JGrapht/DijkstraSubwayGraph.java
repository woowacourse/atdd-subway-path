package wooteco.subway.admin.domain.graph.JGrapht;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.graph.SubwayGraph;
import wooteco.subway.admin.domain.graph.SubwayPath;
import wooteco.subway.admin.exception.NotFoundPathException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class DijkstraSubwayGraph implements SubwayGraph {
    private WeightedMultigraph<Station, LineStationEdge> graph;

    public DijkstraSubwayGraph(List<Line> lines, List<Station> stations, Function<LineStation, Integer> weightStrategy) {
        this.graph = initialize(lines, mapStations(stations), weightStrategy);
    }

    private Map<Long, Station> mapStations(List<Station> stations) {
        return stations.stream()
                .collect(toMap(Station::getId, Function.identity()));
    }

    private WeightedMultigraph<Station, LineStationEdge> initialize(List<Line> lines, Map<Long, Station> stations, Function<LineStation, Integer> weightStrategy) {
        WeightedMultigraph<Station, LineStationEdge> graph = new WeightedMultigraph<>(LineStationEdge.class);

        List<LineStation> lineStations = linesToLineStations(lines);

        stations.values()
                .forEach(graph::addVertex);

        lineStations.forEach(lineStation -> {
            graph.addEdge(stations.get(lineStation.getPreStationId()),
                    stations.get(lineStation.getStationId()),
                    new LineStationEdge(lineStation, weightStrategy));
        });

        return graph;
    }

    private List<LineStation> linesToLineStations(List<Line> lines) {
        return lines.stream()
                .map(Line::getStations)
                .flatMap(Set::stream)
                .filter(LineStation::isNotStarting)
                .collect(Collectors.toList());
    }

    @Override
    public SubwayPath generatePath(Station source, Station target) {
        GraphPath<Station, LineStationEdge> graphPath = DijkstraShortestPath.findPathBetween(graph, source, target);
        validateNotNull(graphPath, source, target);

        int distance = 0;
        int duration = 0;
        for (LineStationEdge lineStationEdge : graphPath.getEdgeList()) {
            distance += lineStationEdge.getDistance();
            duration += lineStationEdge.getDuration();
        }

        return new SubwayPath(graphPath.getVertexList(), distance, duration);
    }

    private void validateNotNull(GraphPath<Station, LineStationEdge> graphPath, Station source, Station target) {
        if (Objects.isNull(graphPath)) {
            throw new NotFoundPathException(source.getName(), target.getName());
        }
    }
}
