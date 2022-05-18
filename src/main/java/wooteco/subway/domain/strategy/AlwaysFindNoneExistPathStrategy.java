package wooteco.subway.domain.strategy;

import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

public class AlwaysFindNoneExistPathStrategy implements FindPathStrategy {

    @Override
    public Path findPath(final Station source, final Station target, final Sections sections) {
        throw notFoundPathException();
    }
}
