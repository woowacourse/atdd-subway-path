package wooteco.subway.domain.path;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgeDisCountPolicy {

    BABY(age -> 0 <= age && age <= 5, money -> 0),
    CHILDREN(age -> 6 <= age && age <= 12,
            money -> (int) ((money - Constants.DEDUCTIBLE) * Constants.CHILDREN_DISCOUNT_RATE)),
    TEENAGER(age -> 13 <= age && age <= 18,
            money -> (int) ((money - Constants.DEDUCTIBLE) * Constants.TEENAGER_DISCOUNT_RATE)),
    ADULT(age -> 19 <= age, money -> money),
    ;

    private final Predicate<Integer> containsAgePredicate;
    private final Function<Integer, Integer> discountPolicy;

    AgeDisCountPolicy(final Predicate<Integer> containsAgePredicate, final Function<Integer, Integer> discountPolicy) {
        this.containsAgePredicate = containsAgePredicate;
        this.discountPolicy = discountPolicy;
    }

    public static AgeDisCountPolicy from(final int age) {
        return Arrays.stream(values())
                .filter(ageDisCountPolicy -> ageDisCountPolicy.containsAgePredicate.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("정상적인 사람 나이가 아닙니다."));
    }

    public int discountedMoney(final int money) {
        return discountPolicy.apply(money);
    }

    private static class Constants {
        private static final int DEDUCTIBLE = 350;
        public static final double CHILDREN_DISCOUNT_RATE = 0.5;
        public static final double TEENAGER_DISCOUNT_RATE = 0.8;
    }
}
