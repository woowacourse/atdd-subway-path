package wooteco.subway.domain.fare.discountpolicy;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountCalculator {

    INFANT_AND_OLD_MAN(age -> age < 6 || age >= 65, new FreeDiscountPolicyStrategy()),
    CHILD(age -> age >= 6 && age < 13, new ChildDiscountPolicyStrategy()),
    TEENAGER(age -> age >= 13 && age < 19, new TeenagerDiscountPolicyStrategy()),
    ADULT(age -> age >= 19 && age < 65, new AdultDiscountPolicyStrategy());

    private static final String NOT_EXIST_MATCHED_AGE = "일치하는 나이가 없습니다.";

    private final Predicate<Integer> predicate;
    private final DiscountPolicyStrategy discountPolicyStrategy;

    DiscountCalculator(Predicate<Integer> predicate, DiscountPolicyStrategy discountPolicyStrategy) {
        this.predicate = predicate;
        this.discountPolicyStrategy = discountPolicyStrategy;
    }

    public static int calculateDiscountedFare(int age, int fare) {
        return getDiscountPolicyStrategy(age).calculateDiscountedFare(fare);
    }

    private static DiscountPolicyStrategy getDiscountPolicyStrategy(int age) {
        return Arrays.stream(values())
                .filter(policy -> policy.predicate.test(age))
                .map(policy -> policy.discountPolicyStrategy)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_MATCHED_AGE));
    }
}
