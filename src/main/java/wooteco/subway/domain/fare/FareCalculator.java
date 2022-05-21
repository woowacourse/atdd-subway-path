package wooteco.subway.domain.fare;

import wooteco.subway.domain.fare.strategy.FareStrategy;

public class FareCalculator {

    private final FareStrategy fareStrategy;

    public FareCalculator(FareStrategy fareStrategy) {
        this.fareStrategy = fareStrategy;
    }

    public int calculate(double distance) {
        return fareStrategy.calculate(distance);
    }
}
