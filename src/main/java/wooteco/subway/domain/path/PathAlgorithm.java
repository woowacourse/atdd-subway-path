package wooteco.subway.domain.path;

import java.util.List;

public interface PathAlgorithm<V, E> {
    List<V> findPath(V from, V to);

    double findDistance(V from, V to);

    List<E> findEdges(V from, V to);
}
