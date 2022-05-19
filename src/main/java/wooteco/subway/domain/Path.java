package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final DijkstraShortestPath dijkstraShortestPath;

    public Path(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        fillVertexes(sections, graph);
        fillEdge(sections, graph);

        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void fillVertexes(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
        }
    }

    private void fillEdge(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public List<Station> getStations(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = getPath(source, target, dijkstraShortestPath);
        validateExistPath(shortestPath);

        return shortestPath.getVertexList();
    }

    public int getDistance(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = getPath(source, target, dijkstraShortestPath);
        validateExistPath(shortestPath);

        return (int) shortestPath.getWeight();
    }

    private GraphPath getPath(Station source, Station target, DijkstraShortestPath dijkstraShortestPath) {
        try {
            return dijkstraShortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("노선에 등록되지 않는 지하철역입니다.");
        }
    }

    private void validateExistPath(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        if (Objects.isNull(shortestPath)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
    }
}
