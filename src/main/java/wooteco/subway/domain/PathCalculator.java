package wooteco.subway.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class PathCalculator {
    private final DijkstraShortestPath<Station, ShortestPathEdge> dijkstraShortestPath;

    private PathCalculator(
            DijkstraShortestPath<Station, ShortestPathEdge> dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static PathCalculator from(List<Line> lines) {
        Set<Station> stations = extractStations(lines);
        WeightedMultigraph<Station, ShortestPathEdge> graph = getMultiGraph(stations, lines);
        return new PathCalculator(new DijkstraShortestPath<>(graph));
    }

    private static Set<Station> extractStations(List<Line> lines) {
        Set<Station> stations = new HashSet<>();
        for (Line line : lines) {
            Sections sections = line.getSections();
            extractSections(stations, sections);
        }
        return stations;
    }

    private static void extractSections(Set<Station> stations, Sections sections) {
        for (Section section : sections.getSections()) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
    }

    private static WeightedMultigraph<Station, ShortestPathEdge> getMultiGraph(Set<Station> stations,
            List<Line> lines) {
        WeightedMultigraph<Station, ShortestPathEdge> graph = new WeightedMultigraph<>(ShortestPathEdge.class);
        addVertex(stations, graph);
        addEdge(lines, graph);
        return graph;
    }

    private static void addVertex(Set<Station> stations, WeightedMultigraph<Station, ShortestPathEdge> graph) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private static void addEdge(List<Line> lines, WeightedMultigraph<Station, ShortestPathEdge> graph) {
        for (Line line : lines) {
            Sections sections = line.getSections();
            addEdgeInfo(graph, line, sections);
        }
    }

    private static void addEdgeInfo(WeightedMultigraph<Station, ShortestPathEdge> graph, Line line, Sections sections) {
        for (Section section : sections.getSections()) {
            graph.addEdge(section.getUpStation(), section.getDownStation(), new ShortestPathEdge(line.getId(),
                    section.getDistance()));
        }
    }

    public List<Station> calculateShortestPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public double calculateShortestDistance(Station source, Station target) {
        return dijkstraShortestPath.getPathWeight(source, target);
    }
}
