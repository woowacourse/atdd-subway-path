package wooteco.subway.domain.strategy.extrafare;

import wooteco.subway.domain.Distance;

public interface ExtraFareStrategy {

    int calculateByDistance(final Distance distance);
}
