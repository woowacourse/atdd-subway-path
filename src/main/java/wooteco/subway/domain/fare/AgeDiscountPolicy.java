package wooteco.subway.domain.fare;

public enum AgeDiscountPolicy {
    CHILDREN(new ChildrenDiscountPolicy()),
    TEENAGER(new TeenagerDiscountPolicy()),
    DEFAULT(new DefaultDiscountPolicy()),
    FREE(new FreeDiscountPolicy());

    private static final int MAX_TEENAGER = 18;
    private static final int MIN_TEENAGER = 13;
    private static final int MAX_CHILDREN = 12;
    private static final int MIN_CHILDREN = 6;
    private static final int MAX_FREE = 65;
    private static final int MIN_FREE = 5;

    private final DiscountPolicy discountPolicy;

    AgeDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public static AgeDiscountPolicy find(int age) {
        if (MIN_CHILDREN <= age && age <= MAX_CHILDREN) {
            return CHILDREN;
        }
        if (MIN_TEENAGER <= age && age <= MAX_TEENAGER) {
            return TEENAGER;
        }
        if (age <= MIN_FREE || age >= MAX_FREE) {
            return FREE;
        }
        return DEFAULT;
    }

    public int calculate(int fare) {
        return discountPolicy.calculate(fare);
    }
}
