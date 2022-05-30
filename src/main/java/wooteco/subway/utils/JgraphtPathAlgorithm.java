package wooteco.subway.utils;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.path.PathAlgorithm;

import java.util.Collection;
import java.util.List;

public class JgraphtPathAlgorithm<V, E> implements PathAlgorithm<V, E> {

    private DijkstraShortestPath<V, E> path;

    public JgraphtPathAlgorithm(Class<E> edgeClass,
                                Collection<V> vertexes,
                                Collection<E> edges) {
        path = init(edgeClass, vertexes, edges);
    }

    public DijkstraShortestPath<V, E> init(Class<E> edgeClass,
                                           Collection<V> vertexes,
                                           Collection<E> edges) {
        WeightedMultigraph<V, E> graph = new WeightedMultigraph<>(edgeClass);
        fillVertexes(graph, vertexes);
        fillEdges(graph, edges);
        return new DijkstraShortestPath<>(graph);
    }

    @Override
    public List<V> findPath(V from, V to) {
        return path.getPath(from, to).getVertexList();
    }

    @Override
    public double findDistance(V from, V to) {
        return path.getPathWeight(from, to);
    }

    @Override
    public List<E> findEdges(V from, V to) {
        return path.getPath(from, to).getEdgeList();
    }

    private void fillVertexes(WeightedMultigraph<V, E> graph, Collection<V> vertexes) {
        for (V vertex : vertexes) {
            graph.addVertex(vertex);
        }
    }

    private void fillEdges(WeightedMultigraph<V, E> graph, Collection<E> edges) {
        for (E edge : edges) {
            V source = ((ConcreteWeightedEdge<V>) edge).getSource();
            V target = ((ConcreteWeightedEdge<V>) edge).getTarget();

            graph.addEdge(source, target, edge);
        }
    }
}
