package wooteco.subway.admin.repository;

import java.util.Optional;
import wooteco.subway.admin.domain.Path;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;

public interface PathRepository {
    Optional<Path> findPath(Station source, Station target, PathType pathType);
}
