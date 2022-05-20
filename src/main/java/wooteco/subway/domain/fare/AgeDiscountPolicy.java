package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import wooteco.subway.exception.AgeDiscountPolicyNotFoundException;

public enum AgeDiscountPolicy {

    TEEN_AGER(AgeDiscountPolicy::isTeenAger, AgeDiscountPolicy::calculateTeenAgerDiscount),
    CHILDREN(AgeDiscountPolicy::isChildren, AgeDiscountPolicy::calculateChildrenDiscount),
    NONE(age -> !isChildren(age) && !isTeenAger(age), fare -> fare);

    private final Predicate<Integer> predicate;

    private final Function<Integer, Integer> function;

    AgeDiscountPolicy(Predicate<Integer> predicate,
                      Function<Integer, Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }

    private static boolean isTeenAger(final Integer age) {
        return age >= 13 && age <= 18;
    }

    private static int calculateTeenAgerDiscount(final Integer fare) {
        return (int) ((fare - 350) * 0.8);
    }

    private static boolean isChildren(Integer age) {
        return age >= 6 && age < 13;
    }

    private static int calculateChildrenDiscount(Integer fare) {
        return (int) ((fare - 350) * 0.5);
    }

    public static AgeDiscountPolicy findPolicy(final int age) {
        return Arrays.stream(values())
                .filter(value -> value.predicate.test(age))
                .findFirst()
                .orElseThrow(AgeDiscountPolicyNotFoundException::new);
    }

    public int discount(final int fare) {
        return function.apply(fare);
    }
}
