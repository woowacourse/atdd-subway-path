package wooteco.subway.domain.strategy.fare;


import static wooteco.subway.domain.strategy.fare.ExtraFareRole.Constants.NO_EXTRA_STANDARD_DISTANCE;

public class BasicExtraFareStrategy implements ExtraFareStrategy {

    private static double BASIC_EXTRA_FARE_STANDARD = 5;

    @Override
    public int calculate(int distance) {
        return (int) (Math.ceil((distance - NO_EXTRA_STANDARD_DISTANCE) / BASIC_EXTRA_FARE_STANDARD)) * FARE_UNIT;
    }
}
