package wooteco.subway.domain.path;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Sections;
import wooteco.subway.exception.NotFoundPathException;
import wooteco.subway.exception.NotFoundStationException;

@Component
public class ShortestPathFindStrategy implements PathFindStrategy {

    @Override
    public List<Long> findPath(final Sections sections, final long sourceId, final long targetId) {
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph = getSubwayGraph(sections);

        final DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        return findShortestPath(sourceId, targetId, dijkstraShortestPath);
    }

    private static List<Long> findShortestPath(final long sourceId, final long targetId,
                                               final DijkstraShortestPath<Long, DefaultWeightedEdge> shortestPath) {

        try {
            return shortestPath.getPath(sourceId, targetId).getVertexList();
        } catch (NullPointerException exception) {
            throw new NotFoundPathException("현재 구간으로 해당 지하철역을 갈 수 없습니다.");
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
            throw new NotFoundStationException("해당 지하철역이 등록이 안되어 있습니다.");
        }
    }

    private static WeightedMultigraph<Long, DefaultWeightedEdge> getSubwayGraph(final Sections sections) {
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        final List<Long> stationIds = sections.getStationIds();
        stationIds.forEach(graph::addVertex);

        sections.getSections().forEach(
                section -> graph.setEdgeWeight(
                        graph.addEdge(section.getUpStationId(), section.getDownStationId()),
                        section.getDistance()
                )
        );
        return graph;
    }
}
