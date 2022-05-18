package wooteco.subway.domain.path;

import java.util.List;

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

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static PathFinder from(LineSeries lineSeries) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        final List<Line> lines = lineSeries.getLines();
        for (Line line : lines) {
            final List<Section> sections = line.getSectionSeries().getSections();
            addStationEdge(graph, sections);
        }
        return new PathFinder(graph);
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
        try {
            return findPath(source, destination).getVertexList();
        } catch (IllegalArgumentException e) {
            throw throwPathNotFound(source, destination);
        }
    }

    private GraphPath<Station, DefaultWeightedEdge> findPath(Station source, Station destination) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph);
        final GraphPath<Station, DefaultWeightedEdge> foundPath = path.getPath(source, destination);
        if (foundPath == null) {
            throw throwPathNotFound(source, destination);
        }
        return foundPath;
    }

    public int getDistance(Station source, Station destination) {
        validateDistinctStation(source, destination);
        try {
            final double weight = findPath(source, destination).getWeight();
            return (int)Math.round(weight);
        } catch (IllegalArgumentException e) {
            throw throwPathNotFound(source, destination);
        }
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
