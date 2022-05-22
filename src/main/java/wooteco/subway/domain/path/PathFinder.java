package wooteco.subway.domain.path;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineSeries;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.PathNotFoundException;

public class PathFinder {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraPath;

    private PathFinder(DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraPath) {
        this.dijkstraPath = dijkstraPath;
    }

    public static PathFinder from(LineSeries lineSeries) {
        return new PathFinder(new DijkstraShortestPath<>(createGraph(lineSeries)));
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> createGraph(LineSeries lineSeries) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Line line : lineSeries.getLines()) {
            addStationEdge(graph, line.getSectionSeries().getSections());
        }
        return graph;
    }

    private static void addStationEdge(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(
                graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance().getValue()
            );
        }
    }

    public List<Station> findShortestPath(Station source, Station destination) {
        validateDistinctStation(source, destination);
        return findPath(source, destination).getVertexList();
    }

    private GraphPath<Station, DefaultWeightedEdge> findPath(Station source, Station destination) {
        try {
            final GraphPath<Station, DefaultWeightedEdge> foundPath = dijkstraPath.getPath(source, destination);
            return Objects.requireNonNull(foundPath);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw throwPathNotFound(source, destination);
        }
    }

    public long getDistance(Station source, Station destination) {
        validateDistinctStation(source, destination);
        return Math.round(findPath(source, destination).getWeight());
    }

    private PathNotFoundException throwPathNotFound(Station source, Station destination) {
        throw new PathNotFoundException(String.format(
            "(%s) 로부터 (%s) 까지의 경로가 존재하지 않습니다.",
            source.getName(),
            destination.getName()
        ));
    }

    private void validateDistinctStation(Station source, Station destination) {
        if (source.equals(destination)) {
            throw new PathNotFoundException(
                String.format("출발지와 도착지(%s)는 동일할 수 없습니다.", source.getName())
            );
        }
    }
}
