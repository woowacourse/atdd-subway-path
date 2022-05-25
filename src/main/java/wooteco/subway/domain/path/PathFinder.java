package wooteco.subway.domain.path;

import java.util.List;

public class PathFinder<E> {
    private PathAlgorithm<Long, E> pathAlgorithm;

    public PathFinder(PathAlgorithm<Long, E> pathAlgorithm) {
        this.pathAlgorithm = pathAlgorithm;
    }

    public List<Long> findPath(Long from, Long to) {
        return pathAlgorithm.findPath(from, to);
    }

    public int findDistance(Long from, Long to) {
        return (int) pathAlgorithm.findDistance(from, to);
    }

    public List<E> findEdges(Long from, Long to) {
        return pathAlgorithm.findEdges(from, to);
    }
}
