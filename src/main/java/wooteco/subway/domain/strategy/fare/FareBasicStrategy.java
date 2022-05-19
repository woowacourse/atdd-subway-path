package wooteco.subway.domain.strategy.fare;

import org.springframework.stereotype.Component;

@Component
public class FareBasicStrategy implements FareStrategy {

    private static final int BASIC_FARE = 1250;
    private static final int SURCHARGE = 100;

    private static final int BASIC_DISTANCE = 10;
    private static final int INTERVAL_ONE = 50;
    private static final int NO_FARE_DISTANCE = 0;

    private static final double INTERVAL_ONE_SURCHARGE_UNIT = 5.0;
    private static final double INTERVAL_TWO_SURCHARGE_UNIT = 8.0;

    @Override
    public int calculateFare(int distance) {
        return BASIC_FARE + calculateIntervalOne(distance) + calculateIntervalTwo(distance);
    }

    private int calculateIntervalOne(int distance) {
        return (int) Math.ceil(Math.min(
                Math.max(distance - BASIC_DISTANCE, NO_FARE_DISTANCE), INTERVAL_ONE - BASIC_DISTANCE) / INTERVAL_ONE_SURCHARGE_UNIT)
                * SURCHARGE;
    }

    private int calculateIntervalTwo(int distance) {
        return (int) Math.ceil(Math.max(distance - INTERVAL_ONE, NO_FARE_DISTANCE) / INTERVAL_TWO_SURCHARGE_UNIT) * SURCHARGE;
    }
}
