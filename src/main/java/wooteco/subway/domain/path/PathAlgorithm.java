package wooteco.subway.domain.path;

import wooteco.subway.domain.Station;

public interface PathAlgorithm {
    Path getPath(Station source, Station target);
}
