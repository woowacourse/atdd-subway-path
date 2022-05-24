package wooteco.subway.domain.path;

import wooteco.subway.domain.fare.PathAlgorithm;

import java.util.List;

public class Path {
    private PathAlgorithm<Long, SectionWeightedEdge> pathAlgorithm;

    public Path(PathAlgorithm<Long, SectionWeightedEdge> pathAlgorithm) {
        this.pathAlgorithm = pathAlgorithm;
    }

    public List<Long> findPath(Long from, Long to) {
        return pathAlgorithm.findPath(from, to);
    }

    public int findDistance(Long from, Long to) {
        return (int) pathAlgorithm.findDistance(from, to);
    }

    public List<SectionWeightedEdge> findEdges(Long from, Long to) {
        return pathAlgorithm.findEdges(from, to);
    }
}
