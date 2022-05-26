package wooteco.subway.domain;

import wooteco.subway.domain.exception.IllegalAgeException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgeDiscountPolicy {

    INFANT(age -> 0 <= age && age < 6, AgeDiscountPolicy::discountInfant),
    CHILD(age -> 6 <= age && age < 13, AgeDiscountPolicy::discountChild),
    TEEN(age -> 13 <= age && age < 19, AgeDiscountPolicy::discountTeen),
    ADULT(age -> 19 <= age, fare -> fare),
    ;

    private final Predicate<Integer> agePredicate;
    private final Function<Integer, Integer> ageDiscountPolicy;

    AgeDiscountPolicy(final Predicate<Integer> agePredicate, final Function<Integer, Integer> ageDiscountPolicy) {
        this.agePredicate = agePredicate;
        this.ageDiscountPolicy = ageDiscountPolicy;
    }

    public static AgeDiscountPolicy from(final int age) {
        return Arrays.stream(values())
                .filter(discountPolicy -> discountPolicy.agePredicate.test(age))
                .findAny()
                .orElseThrow(IllegalAgeException::new);
    }

    private static int discountInfant(final int fare) {
        return 0;
    }

    private static int discountTeen(final int fare) {
        return (int) ((fare - DiscountConstants.DEDUCTION_AMOUNT) * DiscountConstants.TEEN_DISCOUNT_RATE);
    }

    private static int discountChild(final int fare) {
        return (int) ((fare - DiscountConstants.DEDUCTION_AMOUNT) * DiscountConstants.CHILD_DISCOUNT_RATE);
    }

    public int getDiscountedFare(final int fare) {
        return ageDiscountPolicy.apply(fare);
    }

    private static class DiscountConstants {
        private static final int DEDUCTION_AMOUNT = 350;
        private static final double TEEN_DISCOUNT_RATE = 0.8;
        private static final double CHILD_DISCOUNT_RATE = 0.5;
    }
}
