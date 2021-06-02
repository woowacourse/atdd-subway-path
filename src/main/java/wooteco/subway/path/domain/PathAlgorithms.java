package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

public interface PathAlgorithms {

    Path findPath(Station source, Station target);

    void resetGraph(List<Section> sections);
}
