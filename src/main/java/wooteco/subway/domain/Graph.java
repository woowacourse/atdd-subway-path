package wooteco.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Graph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public Graph() {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public void addSections(final Sections sections) {
        addVertexes(sections);
        addEdges(sections);
    }

    private void addVertexes(final Sections sections) {
        final List<Station> stations = sections.extractStations();
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addEdges(final Sections sections) {
        final List<Section> sectionsForEdge = sections.getSections();
        for (Section section : sectionsForEdge) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public ShortestPath getShortestPath(final Station source, final Station target) {
        validateEmpty();
        validateVertexesExist(source, target);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        final GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        return new ShortestPath(path);
    }

    private void validateEmpty() {
        if (graph.vertexSet().isEmpty()) {
            throw new IllegalStateException("그래프가 초기화되지 않았습니다.");
        }
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
}
