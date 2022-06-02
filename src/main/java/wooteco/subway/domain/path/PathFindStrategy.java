package wooteco.subway.domain.path;

import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathFindSpecification;

public interface PathFindStrategy {

    Path findPath(PathFindSpecification specification);
}
