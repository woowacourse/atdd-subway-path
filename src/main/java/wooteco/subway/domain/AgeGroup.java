package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

public enum AgeGroup {
    BABY(age -> 0 <= age && age < 6, value -> 0),
    CHILDREN(age -> 6 <= age && age < 13, value -> value - (value - 350) / 2),
    TEENAGER(age -> 13 <= age && age < 19, value -> value - (value - 350) / 5),
    ADULT(age -> 19 <= age, value -> value);

    private final Predicate<Integer> grouping;
    private final IntUnaryOperator discountValue;

    AgeGroup(Predicate<Integer> grouping, IntUnaryOperator discountValue) {
        this.grouping = grouping;
        this.discountValue = discountValue;
    }

    public static AgeGroup from(int age) {
        return Arrays.stream(AgeGroup.values())
                .filter(ageGroup -> ageGroup.grouping.test(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 연령대가 존재하지 않습니다."));
    }

    public int getDiscountValue(int value) {
        return discountValue.applyAsInt(value);
    }
}
