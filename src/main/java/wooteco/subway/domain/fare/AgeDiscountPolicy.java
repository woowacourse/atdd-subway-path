package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgeDiscountPolicy {

    NONE(age -> age < 6 || age >= 65, 0, 0),
    CHILDREN(age -> age >= 6 && age < 13, 350, 0.5),
    TEENAGER(age -> age >= 13 && age < 19, 350, 0.8),
    ADULT(age -> age >= 19 && age < 65, 0, 1);

    private final Predicate<Integer> predicate;
    private final int defaultDiscount;
    private final double discountRatio;

    AgeDiscountPolicy(Predicate<Integer> predicate, int defaultDiscount, double discountRatio) {
        this.predicate = predicate;
        this.defaultDiscount = defaultDiscount;
        this.discountRatio = discountRatio;
    }

    public static AgeDiscountPolicy of(int age) {
        return Arrays.stream(AgeDiscountPolicy.values())
                .filter(it -> it.predicate.test(age))
                .findFirst()
                .orElse(NONE);
    }

    public int discount(int fare) {
        return (int) ((fare - defaultDiscount) * discountRatio);
    }
}





