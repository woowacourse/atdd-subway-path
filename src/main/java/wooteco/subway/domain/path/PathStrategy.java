package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public interface PathStrategy {

    Path findPath(List<Station> stations, List<Section> sections, Station from, Station to);
}
