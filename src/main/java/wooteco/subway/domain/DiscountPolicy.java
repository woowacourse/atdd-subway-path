package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public enum DiscountPolicy {
    TEENAGER(age -> age >= 13 && age < 19, value -> (int) (value - ((value - 350) * 0.2)));

    private final IntPredicate agePolicy;
    private final IntUnaryOperator fareDiscountPolicy;

    DiscountPolicy(IntPredicate agePolicy, IntUnaryOperator fareDiscountPolicy) {
        this.agePolicy = agePolicy;
        this.fareDiscountPolicy = fareDiscountPolicy;
    }

    public static int getDiscountValue(int fare, int age) {
        return findByAge(age).getFareDiscountPolicy().applyAsInt(fare);
    }

    private static DiscountPolicy findByAge(int age) {
        return Arrays.stream(DiscountPolicy.values())
            .filter(ageGroup -> ageGroup.getAgePolicy().test(age))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("일치하는 그룹이 존재하지 않습니다."));
    }

    public IntPredicate getAgePolicy() {
        return agePolicy;
    }

    private IntUnaryOperator getFareDiscountPolicy() {
        return fareDiscountPolicy;
    }
}
