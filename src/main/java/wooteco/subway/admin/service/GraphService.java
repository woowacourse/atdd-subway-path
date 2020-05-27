package wooteco.subway.admin.service;

import java.util.Optional;
import wooteco.subway.admin.domain.Path;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;

public interface GraphService {
    Optional<Path> findPath(Station source, Station target, PathType pathType);
}
