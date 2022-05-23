package wooteco.subway.domain.path.graphpath;

import java.util.List;

public interface SubwayGraphPath<V, E> {

    List<V> getVertexList();

    List<E> getEdgeList();

    double getWeight();
}
