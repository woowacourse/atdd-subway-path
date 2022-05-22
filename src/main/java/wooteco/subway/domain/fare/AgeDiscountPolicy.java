package wooteco.subway.domain.fare;

import wooteco.subway.domain.fare.vo.Age;

public enum AgeDiscountPolicy {
    CHILDREN(new ChildrenDiscountPolicy()),
    TEENAGER(new TeenagerDiscountPolicy()),
    DEFAULT(new DefaultDiscountPolicy()),
    FREE(new FreeDiscountPolicy());

    private final DiscountPolicy discountPolicy;

    AgeDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public static AgeDiscountPolicy find(Age age) {
        if (age.isChildren()) {
            return CHILDREN;
        }
        if (age.isTeenager()) {
            return TEENAGER;
        }
        if (age.isOldOrBaby()) {
            return FREE;
        }
        return DEFAULT;
    }

    public int calculate(int fare) {
        return discountPolicy.calculate(fare);
    }
}
