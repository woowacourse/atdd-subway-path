package wooteco.subway.domain.fare.strategy;

import wooteco.subway.domain.fare.strategy.AgeStrategy;

public class BabyFare implements AgeStrategy {

    @Override
    public double calculate(int currentFare) {
        return 0;
    }
}
