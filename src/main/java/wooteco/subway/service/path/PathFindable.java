package wooteco.subway.service.path;

import java.util.List;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.Path;

public interface PathFindable {

    Path findPath(List<Section> sections, Station source, Station target);
}
