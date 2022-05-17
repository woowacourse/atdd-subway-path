package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> path;

    public SubwayGraph(List<Section> sections) {
        this.path = createPath(new ArrayList<>(sections));
    }

    public List<Station> getShortestRoute(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> result = path.getPath(source, target);
        validateRoute(result);
        return result.getVertexList();
    }

    public int getShortestDistance(Station source, Station target) {
        return (int) path.getPath(source, target).getWeight();
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> createPath(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
        return new DijkstraShortestPath<>(graph);
    }

    private void validateRoute(GraphPath<Station, DefaultWeightedEdge> result) {
        if (result == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
    }
}
