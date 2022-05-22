package wooteco.subway.domain.fare;

import wooteco.subway.domain.fare.strategy.AgeStrategy;

public class FareCalculator {
    private final AgeStrategy ageStrategy;

    public FareCalculator(AgeStrategy ageStrategy) {
        this.ageStrategy = ageStrategy;
    }

    public double calculateAgeFare(int currentFare){
        return ageStrategy.calculate(currentFare);
    }
}
