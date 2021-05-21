package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.exception.path.SubwayMapException;
import wooteco.subway.section.domain.Section;

import java.util.List;

public class SubwayMap {

    private final ShortestPathStrategy shortestPathStrategy;
    private final List<Section> sections;

    public SubwayMap(ShortestPathStrategy shortestPathStrategy, List<Section> sections) {
        this.shortestPathStrategy = shortestPathStrategy;
        this.sections = sections;
    }

    public List<Long> findShortestPath(long sourceStationId, long targetStationId) {
        return findPath(sourceStationId, targetStationId)
                .getVertexList();
    }

    private GraphPath<Long, DefaultWeightedEdge> findPath(long sourceStationId, long targetStationId) {
        try {
            return shortestPathStrategy.generateAlgorithm(sections)
                    .getPath(sourceStationId, targetStationId);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new SubwayMapException();
        }
    }

    public int findShortestDistance(long sourceStationId, long targetStationId) {
        return (int) findPath(sourceStationId, targetStationId)
                .getWeight();
    }
}
