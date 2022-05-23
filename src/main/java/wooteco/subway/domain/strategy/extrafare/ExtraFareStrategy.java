package wooteco.subway.domain.strategy.extrafare;

import wooteco.subway.domain.Distance;

public interface ExtraFareStrategy {

    int calculate(final Distance distance);
}
