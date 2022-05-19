package wooteco.subway.domain.strategy;

import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import java.util.Optional;

public interface ShortestPathStrategy {

    Optional<Path> findPath(final Station source, final Station target, final Sections sections);
}
