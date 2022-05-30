package wooteco.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {
    private final PathAlgorithm<Long, SectionWeightedEdge> pathAlgorithm;

    public PathFinder(PathAlgorithm<Long, SectionWeightedEdge> pathAlgorithm) {
        this.pathAlgorithm = pathAlgorithm;
    }

    public List<Long> findPath(Long from, Long to) {
        return pathAlgorithm.findPath(from, to);
    }

    public int findDistance(Long from, Long to) {
        return (int) pathAlgorithm.findDistance(from, to);
    }

    public List<Long> getLinesAlongSections(Long from, Long to) {
        return findEdges(from, to).stream()
                .map(SectionWeightedEdge::getLineId)
                .collect(Collectors.toUnmodifiableList());
    }

    private List<SectionWeightedEdge> findEdges(Long from, Long to) {
        return pathAlgorithm.findEdges(from, to);
    }
}
