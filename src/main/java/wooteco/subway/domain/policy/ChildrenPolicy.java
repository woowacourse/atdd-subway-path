package wooteco.subway.domain.policy;

public class ChildrenPolicy extends NeedCalculateDiscountPolicy {

    private static final double CHILDREN_DISCOUNT_RATE = 0.5;
    private static final int CHILDREN_AGE_START = 6;
    private static final int CHILDREN_AGE_END = 13;

    @Override
    public int calculateDiscountFare(int fare) {
        return (int) ((fare - BASE_FARE) * CHILDREN_DISCOUNT_RATE);
    }

    @Override
    public boolean checkAgeRange(int age) {
        return age >= CHILDREN_AGE_START && age < CHILDREN_AGE_END;
    }
}
