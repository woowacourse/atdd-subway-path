package wooteco.subway.domain.property.fare;

import wooteco.subway.exception.NegativeFareException;

public class Fare {

    private static final int BASIC_AMOUNT = 1250;

    private final int amount;

    public Fare() {
        this(BASIC_AMOUNT);
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

    public Fare surcharge(int amount) {
        return new Fare(this.amount + amount);
    }

    public Fare discount(int amount) {
        return new Fare(this.amount - amount);
    }

    public int getAmount() {
        return amount;
    }
}
