package wooteco.subway.admin.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class SubwayGraph {
    private WeightedMultigraph<Long, SubwayWeightEdge> graph;

    public SubwayGraph(Set<Edge> edges, Function<Edge, Integer> edgeIntegerFunction) {
        WeightedMultigraph<Long, SubwayWeightEdge> graph = new WeightedMultigraph<>(SubwayWeightEdge.class);
        for (Edge edge : edges) {
            addEdge(graph, edge, edgeIntegerFunction);
        }
        this.graph = graph;
    }

    private void addEdge(final WeightedMultigraph<Long, SubwayWeightEdge> graph, final Edge edge, final Function<Edge, Integer> edgeIntegerFunction) {
        graph.addVertex(edge.getStationId());
        if (edge.isNotFirst()) {
            graph.addVertex(edge.getPreStationId());
            graph.addEdge(edge.getPreStationId(), edge.getStationId(), new SubwayWeightEdge(edge, edgeIntegerFunction));
        }
    }

    public SubwayPath getPath(Long sourceStationId, Long targetStationId) {
        if (!containAllVertexes(sourceStationId, targetStationId)) {
            throw new IllegalArgumentException(String.format("%d - %d : 존재하지 않는 경로입니다.", sourceStationId, targetStationId));
        }
        return new SubwayPath(DijkstraShortestPath.findPathBetween(graph, sourceStationId, targetStationId));
    }

    public boolean containAllVertexes(final Long sourceStationId, final Long targetStationId) {
        return graph.containsVertex(sourceStationId) && graph.containsVertex(targetStationId);
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SubwayGraph that = (SubwayGraph) o;
        return Objects.equals(graph, that.graph);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph);
    }
}
