package wooteco.subway.domain.strategy.fare.basic;

import org.springframework.stereotype.Component;

@Component
public class IntermediateDistanceFareStrategy implements DistanceFareStrategy {

    private static final int BASIC_FARE = 1250;
    private static final int FIRST_SURCHARGE = 100;

    private static final int BASIC_DISTANCE = 10;
    private static final int INTERVAL_ONE = 50;
    private static final int NO_FARE_DISTANCE = 0;

    private static final double INTERVAL_ONE_SURCHARGE_UNIT = 5.0;

    @Override
    public int calculateFare(int distance) {
        return BASIC_FARE + calculateIntervalOne(distance);
    }

    private int calculateIntervalOne(int distance) {
        return (int) Math.ceil(Math.min(
                Math.max(distance - BASIC_DISTANCE, NO_FARE_DISTANCE), INTERVAL_ONE - BASIC_DISTANCE) / INTERVAL_ONE_SURCHARGE_UNIT)
                * FIRST_SURCHARGE;
    }
}
