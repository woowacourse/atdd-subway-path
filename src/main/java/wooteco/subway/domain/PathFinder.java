package wooteco.subway.domain;

import java.util.List;

public interface PathFinder {
    Path findShortestPath(List<Section> sections, Long sourceId, Long targetId);
}
