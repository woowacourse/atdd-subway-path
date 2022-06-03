package wooteco.subway.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Section;

import java.util.List;

public class PathDijkstraDecisionStrategy implements PathDecisionStrategy {

    private DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath;

    @Override
    public PathDecision decidePath(final List<Section> sections, final Path path) {
        createDijkstraGraph(sections);

        GraphPath<Long, DefaultWeightedEdge> graph = dijkstraShortestPath.getPath(path.getSource(), path.getTarget());
        return new PathDecision(graph.getVertexList(), graph.getWeight());
    }

    private void createDijkstraGraph(final List<Section> sections) {
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Section section : sections) {
            addSectionInGraph(graph, section);
        }
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addSectionInGraph(final WeightedMultigraph<Long, DefaultWeightedEdge> graph, final Section section) {
        final Long upStationId = section.getUpStationId();
        final Long downStationId = section.getDownStationId();

        graph.addVertex(upStationId);
        graph.addVertex(downStationId);
        graph.setEdgeWeight(graph.addEdge(upStationId, downStationId), section.getDistance());
        graph.setEdgeWeight(graph.addEdge(downStationId, upStationId), section.getDistance());
    }
}
