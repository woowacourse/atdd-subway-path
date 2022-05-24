package wooteco.subway.domain.farepolicy;

public class TeenagerPolicy implements FarePolicy {

    private static final double DISCOUNT_RATE = 0.8;

    @Override
    public int calculate(int basicFare) {
        return (int) ((basicFare - DEDUCTED_AMOUNT) * DISCOUNT_RATE);
    }
}
