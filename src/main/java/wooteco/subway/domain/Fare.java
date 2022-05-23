package wooteco.subway.domain;

import wooteco.subway.domain.farestrategy.FareStrategy;

public class Fare {

    private final Long feeAmount;

    public Fare(Long feeAmount) {
        this.feeAmount = feeAmount;
    }

    public static Fare calculateOf(FareStrategy... strategies) {
        long fareAmount = 0;
        for (FareStrategy strategy : strategies) {
            fareAmount = strategy.calculate(fareAmount);
        }
        return new Fare(fareAmount);
    }

    public Long getValue() {
        return feeAmount;
    }
}
