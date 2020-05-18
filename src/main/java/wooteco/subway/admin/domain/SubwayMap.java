package wooteco.subway.admin.domain;

import java.util.List;

public interface SubwayMap {
    List<Long> findShortestPath(Long source, Long target);

    int getPathWeight(Long sourceId, Long targetId);
}
