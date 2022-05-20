package wooteco.subway.domain;

import wooteco.subway.domain.strategy.FareStrategy;

public class Fare {

    private final int basicFare;

    public Fare(int basicFare) {
        this.basicFare = basicFare;
    }

    public int calculateFare(int distance, FareStrategy fareStrategy) {
        return basicFare + fareStrategy.calculate(distance);
    }
}
