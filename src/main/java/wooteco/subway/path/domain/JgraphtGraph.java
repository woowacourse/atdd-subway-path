package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class JgraphtGraph<T> implements DijkstraGraph<T> {
    private final WeightedMultigraph<T, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    @Override
    public void add(T vertex1, T vertex2, int weight) {
        graph.addVertex(vertex1);
        graph.addVertex(vertex2);
        graph.setEdgeWeight(
                graph.addEdge(vertex1, vertex2),
                weight
        );
    }

    @Override
    public List<T> getShortestPath(T from, T to) {
        return createGraphPath(from, to).getVertexList();
    }

    @Override
    public int getShortedDistance(T from, T to) {
        return (int)createGraphPath(from, to).getWeight();
    }

    private GraphPath<T, DefaultWeightedEdge> createGraphPath(T from, T to) {
        DijkstraShortestPath<T, DefaultWeightedEdge> dijkstraShortestPath
                = new DijkstraShortestPath<>(graph);

        return dijkstraShortestPath.getPath(from, to);
    }

}
