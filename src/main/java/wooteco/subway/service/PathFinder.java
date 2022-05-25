package wooteco.subway.service;

import java.util.List;
import wooteco.subway.domain.Section;

public interface PathFinder {

    List<Section> getShortestPathSections();

    int getTotalDistance();

    List<Long> getShortestPath();
}
