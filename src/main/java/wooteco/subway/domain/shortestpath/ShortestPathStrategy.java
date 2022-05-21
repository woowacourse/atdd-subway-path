package wooteco.subway.domain.shortestpath;

import java.util.List;
import wooteco.subway.domain.Sections;

public interface ShortestPathStrategy {

    List<Long> findShortestPath(Sections sections, Long sourceId, Long targetId);
}
