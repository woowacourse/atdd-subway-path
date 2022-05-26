package wooteco.subway.domain.fare.strategy;


import wooteco.subway.domain.fare.strategy.ExtraFareStrategyMapper.Constants;

public class BasicExtraFareStrategy implements ExtraFareStrategy {

    private static double BASIC_EXTRA_FARE_STANDARD = 5;

    @Override
    public int calculate(int distance) {
        return (int) (Math.ceil((distance - Constants.NO_EXTRA_STANDARD_DISTANCE) / BASIC_EXTRA_FARE_STANDARD))
                * FARE_UNIT;
    }
}
