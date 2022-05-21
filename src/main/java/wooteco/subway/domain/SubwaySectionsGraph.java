package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwaySectionsGraph {

    private static final String UNREGISTERED_SECTION_STATION = "노선에 등록되지 않은 지하철역입니다.";
    private static final String NOT_EXIST_PATH = "경로가 존재하지 않습니다.";

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public SubwaySectionsGraph(List<Section> sections) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        fillVertex(sections);
        fillEdge(sections);
    }

    private void fillVertex(List<Section> sections) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
        }
    }

    private void fillEdge(List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public Path getShortestPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = getDijkstraShortestPath(source, target);
        validateExistPath(shortestPath);

        return new Path(shortestPath.getVertexList(), (int) shortestPath.getWeight());
    }

    private GraphPath<Station, DefaultWeightedEdge> getDijkstraShortestPath(Station source, Station target) {
        try {
            return new DijkstraShortestPath<>(graph).getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(UNREGISTERED_SECTION_STATION);
        }
    }

    private void validateExistPath(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        if (Objects.isNull(shortestPath)) {
            throw new IllegalArgumentException(NOT_EXIST_PATH);
        }
    }
}
