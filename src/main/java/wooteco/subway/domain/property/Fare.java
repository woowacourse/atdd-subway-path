package wooteco.subway.domain.property;

import wooteco.subway.exception.NegativeFareException;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int EXTRA_FARE = 100;
    private static final Fare DEFAULT_CACHE = new Fare(BASIC_FARE);

    private static final int BASE_DISTANCE_THRESHOLD = 10;
    private static final int EXTRA_FARE_DISTANCE_THRESHOLD = 50;

    private static final int LOW_EXTRA_UNIT = 5;
    private static final int HIGH_EXTRA_UNIT = 8;

    private final int amount;

    public Fare(int amount) {
        validateNotPositive(amount);
        this.amount = amount;
    }

    public static Fare from(Distance distance) {
        return ofDefault().calculate(distance);
    }

    public static Fare ofDefault() {
        return DEFAULT_CACHE;
    }

    private void validateNotPositive(int amount) {
        if (amount < 0) {
            throw new NegativeFareException("운임 요금은 음수일 수 없습니다.");
        }
    }

    public Fare calculate(Distance distance) {
        int result = amount;

        final int value = distance.getValue();
        result += Math.min(
            calculateExtraFare(value - BASE_DISTANCE_THRESHOLD, LOW_EXTRA_UNIT),
            HIGH_EXTRA_UNIT * EXTRA_FARE
        );
        result += calculateExtraFare(value - EXTRA_FARE_DISTANCE_THRESHOLD, HIGH_EXTRA_UNIT);

        return new Fare(result);
    }

    private int calculateExtraFare(int distance, int unit) {
        if (distance <= 0) {
            return 0;
        }
        return (int)((Math.ceil((distance - 1) / unit) + 1) * EXTRA_FARE);
    }

    public int getAmount() {
        return amount;
    }
}
