package wooteco.subway.domain;

import wooteco.subway.domain.strategy.FareStrategy;

public class Fare {

    private static final int BASIC_FARE = 1250;

    public int calculateFare(int distance, FareStrategy fareStrategy) {
        return BASIC_FARE + fareStrategy.calculate(distance);
    }
}
