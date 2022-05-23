package wooteco.subway.domain;

import static wooteco.subway.domain.AgeGroup.Constants.*;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

public enum AgeGroup {
    BABY(age -> BABY_MIN_AGE <= age && age < CHILD_MIN_AGE, value -> FREE),

    CHILDREN(age -> CHILD_MIN_AGE <= age && age < TEENAGE_MIN_AGE,
            value -> (int) (value - (value - COMMON_DC_FARE) * CHILD_DC_RATE)),

    TEENAGER(age -> TEENAGE_MIN_AGE <= age && age < ADULT_MIN_AGE,
            value -> (int) (value - (value - COMMON_DC_FARE) * TEENAGER_DC_RATE)),

    ADULT(age -> ADULT_MIN_AGE <= age, value -> value);

    private final Predicate<Integer> grouping;
    private final IntUnaryOperator discountedValue;

    AgeGroup(Predicate<Integer> grouping, IntUnaryOperator discountValue) {
        this.grouping = grouping;
        this.discountedValue = discountValue;
    }

    public static AgeGroup from(int age) {
        return Arrays.stream(AgeGroup.values())
                .filter(ageGroup -> ageGroup.grouping.test(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 연령대가 존재하지 않습니다."));
    }

    public int getDiscountedValue(int value) {
        return discountedValue.applyAsInt(value);
    }

    public static class Constants {
        public static final int FREE = 0;
        public static final int BABY_MIN_AGE = 0;
        public static final int CHILD_MIN_AGE = 6;
        public static final int TEENAGE_MIN_AGE = 13;
        public static final int ADULT_MIN_AGE = 19;
        public static final int COMMON_DC_FARE = 350;
        public static final double CHILD_DC_RATE = 0.5;
        public static final double TEENAGER_DC_RATE = 0.2;
    }
}
