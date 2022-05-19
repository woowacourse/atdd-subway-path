package wooteco.subway.domain;

import java.util.List;

public interface PathStrategy {

    Path findPath(List<Station> stations, List<Section> sections, Station from, Station to);
}
