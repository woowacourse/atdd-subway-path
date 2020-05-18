package wooteco.subway.admin.domain.graph.jgraph;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.graph.Graph;
import wooteco.subway.admin.domain.graph.SubwayPath;
import wooteco.subway.admin.domain.graph.WeightEdge;
import wooteco.subway.admin.exception.IllegalPathRequestException;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class SubwayJGraph implements Graph {
    private WeightedMultigraph<Long, WeightEdge> graph;

    public SubwayJGraph(Set<Edge> edges, Function<Edge, Integer> edgeIntegerFunction) {
        WeightedMultigraph<Long, WeightEdge> graph = new WeightedMultigraph<>(SubwayJWeightEdge.class);
        for (Edge edge : edges) {
            addEdge(graph, edge, edgeIntegerFunction);
        }
        this.graph = graph;
    }

    private void addEdge(final WeightedMultigraph<Long, WeightEdge> graph, final Edge edge, final Function<Edge, Integer> edgeIntegerFunction) {
        graph.addVertex(edge.getStationId());
        if (edge.isNotFirst()) {
            graph.addVertex(edge.getPreStationId());
            graph.addEdge(edge.getPreStationId(), edge.getStationId(), new SubwayJWeightEdge(edge, edgeIntegerFunction));
        }
    }

    @Override
    public SubwayPath getPath(Long sourceStationId, Long targetStationId) {
        validateContainStations(sourceStationId, targetStationId);

        GraphPath<Long, WeightEdge> path = DijkstraShortestPath.findPathBetween(graph, sourceStationId, targetStationId);
        validateLinkedEdge(sourceStationId, targetStationId, path);

        return new SubwayPath(path.getEdgeList());
    }

    private void validateLinkedEdge(final Long sourceStationId, final Long targetStationId, final GraphPath<Long, WeightEdge> path) {
        if (Objects.isNull(path)) {
            throw new IllegalPathRequestException(String.format("%d - %d : 존재하지 않는 경로입니다.", sourceStationId, targetStationId));
        }
    }

    private void validateContainStations(final Long sourceStationId, final Long targetStationId) {
        if (!containAllVertexes(sourceStationId, targetStationId)) {
            throw new IllegalArgumentException(String.format("%d - %d : 해당 역이 존재하지 않습니다.", sourceStationId, targetStationId));
        }
    }

    @Override
    public boolean containAllVertexes(final Long sourceStationId, final Long targetStationId) {
        return graph.containsVertex(sourceStationId) && graph.containsVertex(targetStationId);
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SubwayJGraph that = (SubwayJGraph) o;
        return Objects.equals(graph, that.graph);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph);
    }
}
