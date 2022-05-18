package wooteco.subway.domain.property;

import wooteco.subway.exception.NegativeFareException;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int EXTRA_FARE = 100;

    private static final int BASE_DISTANCE_THRESHOLD = 10;
    private static final int EXTRA_FARE_DISTANCE_THRESHOLD = 50;

    private static final int LOW_EXTRA_UNIT = 5;
    private static final int HIGH_EXTRA_UNIT = 8;

    private final int amount;

    public Fare(int distance) {
        amount = calculateFare(distance);
    }

    private int calculateFare(int distance) {
        int result = BASIC_FARE;

        result += Math.min(
                calculateExtraFare(distance - BASE_DISTANCE_THRESHOLD, LOW_EXTRA_UNIT),
                HIGH_EXTRA_UNIT * EXTRA_FARE
        );
        result += calculateExtraFare(distance - EXTRA_FARE_DISTANCE_THRESHOLD, HIGH_EXTRA_UNIT);

        return result;
    }

    private int calculateExtraFare(int distance, int unit) {
        if (distance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * EXTRA_FARE);
    }

    public int getAmount() {
        return amount;
    }
}
