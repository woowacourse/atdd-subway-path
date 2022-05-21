package wooteco.subway.domain.path.fare2;

public class AgeDiscountFare extends Decorator {

    private static final int BABY_CHILD_AGE_THRESHOLD = 6;
    private static final double CHILD_DISCOUNT_RATIO = 0.5;
    private static final int CHILD_ADOLESCENT_THRESHOLD = 13;
    private static final double ADOLESCENT_DISCOUNT_RATIO = 0.2;
    private static final int ADOLESCENT_ADULT_THRESHOLD = 19;
    private static final int ELDERLY_AGE_THRESHOLD = 65;
    private static final int AGE_BASIC_DISCOUNT_AMOUNT = 350;

    private final int age;

    public AgeDiscountFare(Fare delegate, int age) {
        super(delegate);
        this.age = age;
    }

    @Override
    public int calculate() {
        int fare = super.delegate();
        if (age < BABY_CHILD_AGE_THRESHOLD || age >= ELDERLY_AGE_THRESHOLD) {
            return 0;
        }
        if (age < CHILD_ADOLESCENT_THRESHOLD) {
            return calculateChileFare(fare, CHILD_DISCOUNT_RATIO);
        }
        if (age < ADOLESCENT_ADULT_THRESHOLD) {
            return calculateChileFare(fare, ADOLESCENT_DISCOUNT_RATIO);
        }
        return fare;
    }

    private int calculateChileFare(int fare, double discountRatio) {
        return (int) ((fare - AGE_BASIC_DISCOUNT_AMOUNT) * (1 - discountRatio));
    }
}
