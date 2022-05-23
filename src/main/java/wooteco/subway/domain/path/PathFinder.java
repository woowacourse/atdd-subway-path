package wooteco.subway.domain.path;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineSeries;
import wooteco.subway.domain.property.Distance;
import wooteco.subway.domain.property.fare.Fare;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.PathNotFoundException;

public class PathFinder {

    private final DijkstraShortestPath<Station, ExtraFareEdge> dijkstraPath;

    private PathFinder(DijkstraShortestPath<Station, ExtraFareEdge> dijkstraPath) {
        this.dijkstraPath = dijkstraPath;
    }

    public static PathFinder from(LineSeries lineSeries) {
        return new PathFinder(new DijkstraShortestPath<>(createGraph(lineSeries)));
    }

    private static WeightedMultigraph<Station, ExtraFareEdge> createGraph(LineSeries lineSeries) {
        WeightedMultigraph<Station, ExtraFareEdge> graph = new WeightedMultigraph<>(ExtraFareEdge.class);
        for (Line line : lineSeries.getLines()) {
            addStationEdge(graph, line.getSectionSeries().getSections(), line.getExtraFare());
        }
        return graph;
    }

    private static void addStationEdge(WeightedMultigraph<Station, ExtraFareEdge> graph, List<Section> sections, Fare lineExtraFare) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            ExtraFareEdge edge = new ExtraFareEdge(lineExtraFare);
            graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
            graph.setEdgeWeight(edge, section.getDistance().getValue());
        }
    }

    public Path findShortestPath(Station source, Station destination) {
        validateDistinctStation(source, destination);
        return toPath(findPath(source, destination));
    }

    private void validateDistinctStation(Station source, Station destination) {
        if (source.equals(destination)) {
            throw new PathNotFoundException(
                String.format("출발지와 도착지(%s)는 동일할 수 없습니다.", source.getName())
            );
        }
    }

    private GraphPath<Station, ExtraFareEdge> findPath(Station source, Station destination) {
        try {
            final GraphPath<Station, ExtraFareEdge> foundPath = dijkstraPath.getPath(source, destination);
            return Objects.requireNonNull(foundPath);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new PathNotFoundException(String.format(
                "(%s) 로부터 (%s) 까지의 경로가 존재하지 않습니다.",
                source.getName(),
                destination.getName()
            ));
        }
    }

    private Path toPath(GraphPath<Station, ExtraFareEdge> pathGraph) {
        return new Path(
            pathGraph.getVertexList(),
            pathGraph.getEdgeList()
                .stream()
                .map(ExtraFareEdge::getExtraFare)
                .collect(Collectors.toList()),
            new Distance(Math.round(pathGraph.getWeight()))
        );
    }
}
