package wooteco.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private final Sections sections;
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    private Path(final Sections sections, final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final DijkstraShortestPath<Station, DefaultWeightedEdge> path) {
        this.sections = sections;
        this.graph = graph;
        this.shortestPath = path;
    }

    public static Path of(final Sections sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertexes(graph, sections);
        addEdges(graph, sections);
        return new Path(sections, graph, new DijkstraShortestPath<>(graph));
    }

    private static void addVertexes(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final Sections sections) {
        for (Station station : sections.extractStations()) {
            graph.addVertex(station);
        }
    }

    private static void addEdges(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final Sections sections) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public ShortestPath getShortestPath(final Station source, final Station target) {
        validateVertexesExist(source, target);
        final GraphPath<Station, DefaultWeightedEdge> path = getPath(source, target);
        final List<Station> stations = path.getVertexList();
        List<Section> connectedSections = getConnectedSections(stations);
        return new ShortestPath(stations, new Sections(connectedSections), (int) path.getWeight());
    }

    private void validateVertexesExist(final Station source, final Station target) {
        validateVertexExist(source);
        validateVertexExist(target);
    }

    private void validateVertexExist(final Station station) {
        if (!graph.containsVertex(station)) {
            throw new IllegalStateException("지하철역이 존재하지 않습니다.");
        }
    }

    private GraphPath<Station, DefaultWeightedEdge> getPath(final Station source, final Station target) {
        final GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(source, target);
        if (path == null) {
            throw new IllegalStateException("해당 경로가 존재하지 않습니다.");
        }
        return path;
    }

    private List<Section> getConnectedSections(final List<Station> stations) {
        List<Section> connectedSections = new ArrayList<>();
        for (int i = 0; i < stations.size() - 1; i++) {
            final Section connectedSection = sections.findConnectedSection(stations.get(i), stations.get(i + 1), (int) shortestPath.getPathWeight(stations.get(i), stations.get(i + 1)));
            connectedSections.add(connectedSection);
        }
        return connectedSections;
    }
}
