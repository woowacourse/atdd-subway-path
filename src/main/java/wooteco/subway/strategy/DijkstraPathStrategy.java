package wooteco.subway.strategy;

import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;

public interface DijkstraPathStrategy {

    Path getPath(Lines lines, Station source, Station target);
}
