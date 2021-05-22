package wooteco.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.exception.NotReachableException;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

public class Path {

    DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public Path(final List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);
        initializeStationsOnGraph(lines, graph);
        initializedSectionOnGraph(lines, graph);
        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void initializeStationsOnGraph(final List<Line> lines,
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lines.stream()
            .flatMap(line -> line.getStations().stream())
            .forEach(graph::addVertex);
    }

    private void initializedSectionOnGraph(final List<Line> lines,
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lines.stream()
            .map(Line::getSections)
            .flatMap(sectionsOnLine -> sectionsOnLine.getSections().stream())
            .forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(),
                section.getDownStation()), section.getDistance()));
    }

    public PathResponse optimizedPath(Station sourceStation, Station targetStation) {
        List<StationResponse> stationResponses = stationConnection(sourceStation, targetStation);
        Double weight = dijkstraShortestPath.getPathWeight(sourceStation, targetStation);
        return new PathResponse(stationResponses, weight);
    }

    private List<StationResponse> stationConnection(Station sourceStation, Station targetStation) {
        try {
            List<Station> shortestPath
                = dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
            return shortestPath.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
        } catch (NullPointerException e) {
            throw new NotReachableException(sourceStation.getId(), targetStation.getId());
        }
    }
}
