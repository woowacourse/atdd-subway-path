package wooteco.subway.domain.fare;

public class AgeDiscountStrategy implements DiscountStrategy{

    private final static int BASE_DISCOUNT_AMOUNT = 350;
    private final static double CHILD_DISCOUNT_PERCENTAGE = 0.5;
    private final static double ADOLESCENT_DISCOUNT_PERCENTAGE = 0.8;

    @Override
    public int discount(Age age, int fare) {
        if (age == Age.CHILD) {
            return (int) ((fare - BASE_DISCOUNT_AMOUNT) * CHILD_DISCOUNT_PERCENTAGE);
        }
        if (age == Age.ADOLESCENT) {
            return (int) ((fare - BASE_DISCOUNT_AMOUNT) * ADOLESCENT_DISCOUNT_PERCENTAGE);
        }
        return fare;
    }
}
