package wooteco.subway.domain.path;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgeDiscountPolicy {
    BABY(age -> 1 <= age && age < 6, fare -> 0),
    CHILDREN(age -> 6 <= age && age < 13,
            fare -> (fare - Constants.DEDUCTIBLE) * (100 - Constants.CHILDREN_DISCOUNT_PERCENT) / 100),
    TEENAGER(age -> 13 <= age && age < 19,
            fare -> (fare - Constants.DEDUCTIBLE) * (100 - Constants.TEENAGER_DISCOUNT_PERCENT) / 100),
    ADULT(age -> 19 <= age, fare -> fare),
    ;

    private final Predicate<Integer> containsAgePredicate;
    private final Function<Integer, Integer> discountPolicy;

    AgeDiscountPolicy(final Predicate<Integer> containsAgePredicate, final Function<Integer, Integer> discountPolicy) {
        this.containsAgePredicate = containsAgePredicate;
        this.discountPolicy = discountPolicy;
    }

    public static AgeDiscountPolicy from(final int age) {
        return Arrays.stream(values())
                .filter(ageDisCountPolicy -> ageDisCountPolicy.containsAgePredicate.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("입력한 나이값이 올바르지 않습니다."));
    }

    public int discountAmount(final int money) {
        return discountPolicy.apply(money);
    }

    private static class Constants {
        private static final int DEDUCTIBLE = 350;
        private static final int CHILDREN_DISCOUNT_PERCENT = 50;
        private static final int TEENAGER_DISCOUNT_PERCENT = 20;
    }
}
