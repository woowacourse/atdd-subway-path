package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Path;

public interface PathGenerator {

    Path findPath(List<Section> sections, Station from, Station to);
}
