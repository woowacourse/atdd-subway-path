package wooteco.subway.admin.domain.path;

import java.util.List;

public interface GraphStrategy<V, E> {
    Graph makeGraph(List<V> vertexList, List<E> edgeList, PathType pathType);
}