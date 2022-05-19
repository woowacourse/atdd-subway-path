package wooteco.subway.domain.path;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

public class PathFinder {

    WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder() {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public Path getShortestPath(Station source, Station target) {
        validateEmpty();
        validateStations(source, target);
        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        return new Path(shortestPath.getPath(source, target));
    }

    private void validateStations(Station source, Station target) {
        if (!graph.containsVertex(source)) {
            throw new IllegalArgumentException(String.format("%s 역이 구간으로 존재하지 않습니다.", source.getName()));
        }
        if (!graph.containsVertex(target)) {
            throw new IllegalArgumentException(String.format("%s 역이 구간으로 존재하지 않습니다.", target.getName()));
        }
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
