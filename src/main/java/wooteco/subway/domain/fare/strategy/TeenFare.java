package wooteco.subway.domain.fare.strategy;

import wooteco.subway.domain.fare.strategy.AgeStrategy;

public class TeenFare implements AgeStrategy {
    @Override
    public double calculate(int currentFare) {
        return (currentFare - 350) * 0.8;
    }
}
