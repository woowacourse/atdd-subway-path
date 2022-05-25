package wooteco.subway.domain.fare;

public class SpecialDiscountStrategy implements DiscountStrategy {

    private static final int FREE_FARE_AMOUNT = 0;

    private static final DiscountStrategy INSTANCE = new SpecialDiscountStrategy();

    private SpecialDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int discount(int fare) {
        return FREE_FARE_AMOUNT;
    }
}
