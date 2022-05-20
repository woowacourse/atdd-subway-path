package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgeDisCount {

    BABY(age -> 0 <= age && age <= 5, money -> 0),
    CHILDREN(age -> 6 <= age && age <= 12, money -> (int) ((money - 350) * 0.5)),
    TEENAGER(age -> 13 <= age && age <= 18, money -> (int) ((money - 350) * 0.8)),
    ADULT(age -> 19 <= age, money -> money),
    ;

    private final Predicate<Integer> containsAgePredicate;
    private final Function<Integer, Integer> discountPolicy;

    AgeDisCount(final Predicate<Integer> containsAgePredicate, final Function<Integer, Integer> discountPolicy) {
        this.containsAgePredicate = containsAgePredicate;
        this.discountPolicy = discountPolicy;
    }

    public static AgeDisCount from(final int age) {
        return Arrays.stream(values())
                .filter(ageDisCount -> ageDisCount.containsAgePredicate.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("정상적인 사람 나이가 아닙니다."));
    }

    public int discountedMoney(final int money) {
        return discountPolicy.apply(money);
    }
}
