package wooteco.subway.domain.fare;

import wooteco.subway.domain.fare.strategy.AgeFareStrategy;

public class FareCalculator {
    private final AgeFareStrategy ageFareStrategy;

    public FareCalculator(AgeFareStrategy ageFareStrategy) {
        this.ageFareStrategy = ageFareStrategy;
    }

    public double calculateAgeFare(int currentFare) {
        return ageFareStrategy.calculate(currentFare);
    }
}
