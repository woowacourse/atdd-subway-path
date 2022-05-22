package wooteco.subway.domain;

public enum AgeDiscountPolicy {
    TEENAGER(new TeenagerDiscountPolicy()),
    DEFAULT(new DefaultDiscountPolicy());

    private static final int MIN_TEENAGER = 13;
    private static final int MAX_TEENAGER = 18;

    private final DiscountPolicy discountPolicy;

    AgeDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public static AgeDiscountPolicy find(int age) {
        if (MIN_TEENAGER <= age && age <= MAX_TEENAGER) {
            return TEENAGER;
        }
        return DEFAULT;
    }

    public int calculate(int fare) {
        return discountPolicy.calculate(fare);
    }
}
