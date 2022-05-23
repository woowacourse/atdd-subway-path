package wooteco.subway.domain.shortestpath;

import java.util.List;
import java.util.Optional;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Sections;
import wooteco.subway.exception.NotFoundPathException;
import wooteco.subway.exception.NotFoundStationException;

public final class DistanceShortestPathStrategy implements ShortestPathStrategy {

    @Override
    public List<Long> findShortestPath(Sections sections, Long sourceId, Long targetId) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = getSubwayGraph(sections);
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        return findShortestPath(sourceId, targetId, dijkstraShortestPath).getVertexList();
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> getSubwayGraph(Sections sections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        List<Long> stationIds = sections.getStationIds();
        stationIds.forEach(graph::addVertex);

        sections.getSections().forEach(
                section -> graph.setEdgeWeight(graph.addEdge(section.getUpStationId(), section.getDownStationId()),
                        section.getDistance())
        );
        return graph;
    }

    private GraphPath<Long, DefaultWeightedEdge> findShortestPath(
            long sourceId, long targetId, DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath) {
        try {
            return Optional.ofNullable(dijkstraShortestPath.getPath(sourceId, targetId))
                    .orElseThrow(() -> new NotFoundPathException("현재 구간으로 해당 지하철역을 갈 수 없습니다."));
        } catch (IllegalArgumentException exception) {
            throw new NotFoundStationException();
        }
    }

}
