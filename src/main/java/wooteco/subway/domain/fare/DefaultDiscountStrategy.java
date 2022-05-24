package wooteco.subway.domain.fare;

public class DefaultDiscountStrategy implements DiscountStrategy {

    private static final DiscountStrategy INSTANCE = new DefaultDiscountStrategy();

    private DefaultDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int discount(int fare) {
        return fare;
    }

}
