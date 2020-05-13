package wooteco.subway.admin.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SubwayGraph {
    private WeightedMultigraph<Long, SubwayWeightEdge> graph;

    public SubwayGraph(Set<Edge> edges) {
        WeightedMultigraph<Long, SubwayWeightEdge> graph = new WeightedMultigraph<>(SubwayWeightEdge.class);
        for (Edge edge : edges) {
            addEdge(graph, edge);
        }
        this.graph = graph;
    }

    public static Comparator<SubwayGraph> weightComparator(final Long sourceStationId, final Long targetStationId) {
        return Comparator.comparingDouble(o -> o.sumOfEdgeWeights(o.getShortestPath(sourceStationId, targetStationId)));
    }

    private void addEdge(final WeightedMultigraph<Long, SubwayWeightEdge> graph, final Edge edge) {
        graph.addVertex(edge.getStationId());
        if (edge.isNotFirst()) {
            graph.addVertex(edge.getPreStationId());
            graph.setEdgeWeight(graph.addEdge(edge.getPreStationId(), edge.getStationId()), edge.getDistance());
        }
    }

    public List<Long> getShortestPath(Long sourceStationId, Long targetStationId) {
        if (!containAllVertexes(sourceStationId, targetStationId)) {
            throw new IllegalArgumentException(String.format("%d - %d : 존재하지 않는 경로입니다.", sourceStationId, targetStationId));
        }
        DijkstraShortestPath<Long, SubwayWeightEdge> dijkstraShortestPath = new DijkstraShortestPath<>(this.graph);
        return dijkstraShortestPath.getPath(sourceStationId, targetStationId)
                .getVertexList();
    }

    public boolean containAllVertexes(final Long sourceStationId, final Long targetStationId) {
        return graph.containsVertex(sourceStationId) && graph.containsVertex(targetStationId);
    }

    public double sumOfEdgeWeights(List<Long> stationIds) {
        double sum = 0;
        for (int idx = 0; idx < stationIds.size() - 1; idx++) {
            sum += graph.getEdge(stationIds.get(idx), stationIds.get(idx + 1)).getValue();
        }
        return sum;
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
