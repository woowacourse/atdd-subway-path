package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class Subway {
    private final List<Line> lines;
    private final List<Station> stations;
    private final WeightType weightType;

    public Subway(List<Line> lines, List<Station> stations, WeightType weightType) {
        validateNull(lines, stations);
        this.lines = lines;
        this.stations = stations;
        this.weightType = weightType;
    }

    private void validateNull(List<Line> lines, List<Station> stations) {
        if (Objects.isNull(lines) || Objects.isNull(stations)) {
            throw new IllegalArgumentException("노선 또는 역이 존재하지 않습니다.");
        }
    }

    public GraphPath<Station, SubwayEdge> findShortestPath(String source, String target) {
        Map<Long, Station> stationMapper = generateStationMapper();

        WeightedMultigraph<Station, SubwayEdge> subwayGraph = new WeightedMultigraph<>(
                SubwayEdge.class);
        addVertices(subwayGraph);
        addEdges(subwayGraph, stationMapper);

        DijkstraShortestPath<Station, SubwayEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
                subwayGraph);
        Station sourceId = findStationByName(source);
        Station targetId = findStationByName(target);

        return dijkstraShortestPath.getPath(sourceId, targetId);
    }

    private Station findStationByName(String source) {
        return stations.stream()
                .filter(station -> station.isSameName(source))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 이름을 가진 지하철 역이 없습니다"));
    }

    private void addVertices(WeightedMultigraph<Station, SubwayEdge> subwayGraph) {
        for (Station station : stations) {
            subwayGraph.addVertex(station);
        }
    }

    private void addEdges(WeightedMultigraph<Station, SubwayEdge> subwayGraph, Map<Long, Station> stationMapper) {
        List<LineStation> lineStations = generateLineStations(lines);
        for (LineStation lineStation : lineStations) {
            SubwayEdge lineStationEdge = subwayGraph.addEdge(stationMapper.get(lineStation.getPreStationId()),
                    stationMapper.get(lineStation.getStationId()));
            subwayGraph.setEdgeWeight(lineStationEdge, weightType.getWeight(lineStation));
            lineStationEdge.setLineStation(lineStation);
        }
    }

    private Map<Long, Station> generateStationMapper() {
        return stations.stream()
                .collect(Collectors.toMap(
                        Station::getId,
                        station -> station));
    }

    private List<LineStation> generateLineStations(List<Line> lines) {
        return lines.stream()
                .flatMap(line -> line.getLineStations().stream())
                .filter(LineStation::hasPreStation)
                .collect(Collectors.toList());
    }
}
