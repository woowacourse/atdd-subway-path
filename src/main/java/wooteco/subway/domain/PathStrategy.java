package wooteco.subway.domain;

import java.util.List;

public interface PathStrategy {

    Path findPath(List<Section> sections, Long source, Long target);
}
