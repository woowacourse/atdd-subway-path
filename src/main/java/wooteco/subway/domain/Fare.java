package wooteco.subway.domain;

import wooteco.subway.domain.strategy.FareStrategy;

public class Fare {

    private static final int BASIC_FARE = 1250;

    private final FareStrategy fareStrategy;

    public Fare(FareStrategy fareStrategy) {
        this.fareStrategy = fareStrategy;
    }

    public int calculateFare(int distance) {
        return BASIC_FARE + fareStrategy.calculate(distance);
    }
}
