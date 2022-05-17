package wooteco.subway.domain.property;

import wooteco.subway.exception.NegativeFareException;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final Distance BASE_DISTANCE = new Distance(10);
    private static final Distance TOP_DISTANCE = new Distance(50);
    private final int amount;

    public Fare() {
        this(BASIC_FARE);
    }

    public Fare(int amount) {
        validateNotPositive(amount);
        this.amount = amount;
    }

    private void validateNotPositive(int amount) {
        if (amount < 0) {
            throw new NegativeFareException("운임 요금은 음수일 수 없습니다.");
        }
    }

    public int getAmount() {
        return amount;
    }

    public Fare calculate(Distance distance) {
        // TODO
        int leftDistance = distance.getValue() - 10;
        int resultAmount = amount;

        if (leftDistance > 0) {
            int extraFareCount = Math.min((leftDistance / 5) + 1, 8);
            resultAmount += extraFareCount * 100;
            leftDistance -= extraFareCount * 5;
        }

        if (leftDistance > 0) {
            resultAmount += (leftDistance / 8) * 100;
        }

        return new Fare(resultAmount);
    }
}
