package wooteco.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Sections;
import wooteco.subway.exception.NotFoundPathException;
import wooteco.subway.exception.NotFoundStationException;

@Component
public class ShortestPathFindStrategy implements PathFindStrategy {

    @Override
    public FindPathResult findPath(final Sections sections, final long sourceId, final long targetId) {
        final WeightedMultigraph<Long, SubwayPathEdge> graph = getSubwayGraph(sections);

        final DijkstraShortestPath<Long, SubwayPathEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        final List<Long> stationIds = findShortestPath(sourceId, targetId, dijkstraShortestPath);
        final List<Long> usedLineIds = findUsedLineIds(sourceId, targetId, dijkstraShortestPath);
        final int totalDistance = findTotalDistance(sourceId, targetId, dijkstraShortestPath);

        return new FindPathResult(stationIds, usedLineIds, totalDistance);
    }

    private List<Long> findShortestPath(final long sourceId, final long targetId,
                                        final DijkstraShortestPath<Long, SubwayPathEdge> shortestPath) {

        try {
            return shortestPath.getPath(sourceId, targetId).getVertexList();
        } catch (NullPointerException exception) {
            throw new NotFoundPathException("현재 구간으로 해당 지하철역을 갈 수 없습니다.");
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
            throw new NotFoundStationException("해당 지하철역이 등록이 안되어 있습니다.");
        }
    }

    private static WeightedMultigraph<Long, SubwayPathEdge> getSubwayGraph(final Sections sections) {
        final WeightedMultigraph<Long, SubwayPathEdge> graph
                = new WeightedMultigraph<>(SubwayPathEdge.class);
        final List<Long> stationIds = sections.getStationIds();
        stationIds.forEach(graph::addVertex);

        sections.getSections().forEach(
                section -> graph.addEdge(
                        section.getUpStationId(),
                        section.getDownStationId(),
                        new SubwayPathEdge(section)
                )
        );
        return graph;
    }

    private List<Long> findUsedLineIds(final long sourceId, final long targetId,
                                       final DijkstraShortestPath<Long, SubwayPathEdge> shortestPath) {
        return shortestPath.getPath(sourceId, targetId)
                .getEdgeList()
                .stream()
                .map(SubwayPathEdge::getLineId)
                .distinct()
                .collect(Collectors.toList());
    }

    private int findTotalDistance(final long sourceId, final long targetId,
                                  final DijkstraShortestPath<Long, SubwayPathEdge> shortestPath) {
        return shortestPath.getPath(sourceId, targetId)
                .getEdgeList()
                .stream()
                .mapToInt(SubwayPathEdge::getDistance)
                .sum();
    }
}
