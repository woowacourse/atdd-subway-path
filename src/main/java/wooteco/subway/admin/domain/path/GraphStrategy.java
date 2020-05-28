package wooteco.subway.admin.domain.path;

import java.util.List;

public interface GraphStrategy<V, E> {
    <W> Graph makeGraph(List<? extends V> vertexList, List<? extends E> edgeList, PathType pathType,
        Class<W> weightClass);
}