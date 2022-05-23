package wooteco.subway.service.pathInfra;

import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;

import java.util.List;

public interface PathFinder {
    Path findShortestPath(List<Section> sections, Long sourceId, Long targetId);
}
