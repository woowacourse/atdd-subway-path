package wooteco.subway.domain;

import java.util.List;

public interface PathGenerator {

    Path findPath(List<Section> sections, Station from, Station to);
}
