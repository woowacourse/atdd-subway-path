package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Graph {

    WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public Graph() {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public ShortestPath getShortestPath(Station source, Station target) {
        validateEmpty();
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return new ShortestPath(dijkstraShortestPath.getPath(source, target));
    }

    private void validateEmpty() {
        if (graph.vertexSet().isEmpty()) {
            throw new IllegalStateException("그래프가 초기화되지 않았습니다.");
        }
    }

    public void addSections(Sections sections) {
        addVertexes(sections);
        addEdges(sections);
    }

    private void addVertexes(Sections sections) {
        List<Station> stations = sections.extractStations();
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addEdges(Sections sections) {
        List<Section> sectionsForEdge = sections.getSections();
        for (Section section : sectionsForEdge) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }
}
