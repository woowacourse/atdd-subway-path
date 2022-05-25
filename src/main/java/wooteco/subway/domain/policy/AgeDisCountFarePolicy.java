package wooteco.subway.domain.policy;

public class AgeDisCountFarePolicy implements FarePolicy {

    private static final int INFANT_AGE_THRESHOLD = 6;
    private static final int CHILD_AGE_THRESHOLD = 13;
    private static final int TEENAGER_AGE_THRESHOLD = 19;
    private static final int DEFAULT_DISCOUNT = 350;
    private static final double CHILE_DISCOUNT_RATE = 0.5;
    private static final double TEENAGER_DISCOUNT_RATE = 0.8;

    private final int age;

    public AgeDisCountFarePolicy(int age) {
        this.age = age;
    }

    @Override
    public int apply(int fare) {
        if (age < INFANT_AGE_THRESHOLD) {
            return 0;
        }
        if (age < CHILD_AGE_THRESHOLD) {
            return (int) (Math.round(fare - DEFAULT_DISCOUNT) * CHILE_DISCOUNT_RATE)
                + DEFAULT_DISCOUNT;
        }
        if (age < TEENAGER_AGE_THRESHOLD) {
            return (int) (Math.round(fare - DEFAULT_DISCOUNT) * TEENAGER_DISCOUNT_RATE)
                + DEFAULT_DISCOUNT;
        }
        return fare;
    }
}
