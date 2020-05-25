package wooteco.subway.admin.domain.path;

import java.util.List;

import wooteco.subway.admin.domain.LineStation;

public interface GraphStrategy {
    void makeGraph(List<Long> vertexList, List<LineStation> edgeList, PathType pathType);

    Path findPath(Long sourceId, Long targetId);
}