package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.section.Section;

public interface PathFinder {

    Path find(List<Long> stationIds, List<Section> sections, Long source, Long target);
}
