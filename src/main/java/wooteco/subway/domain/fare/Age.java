package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum Age {
    BABY(Age::isBaby, fare -> 0),
    CHILD(Age::isChild, Age::calculateFareForChild),
    TEENAGER(Age::isTeenager, Age::calculateFareForTeenager),
    ADULT(Age::isAdult, fare -> fare);

    private static final int CHILD_AGE = 6;
    private static final int TEENAGER_AGE = 13;
    private static final int ADULT_AGE = 19;

    private static final int BASE_DEDUCTION = 350;

    private static final double DISCOUNT_RATE_FOR_CHILD = 0.5;
    private static final double DISCOUNT_RATE_FOR_TEENAGER = 0.8;

    private final Predicate<Integer> ageCondition;
    private final Function<Integer, Integer> discountPolicy;

    Age(Predicate<Integer> ageCondition, Function<Integer, Integer> discountBy) {
        this.ageCondition = ageCondition;
        this.discountPolicy = discountBy;
    }

    public static int discountBy(int age, int baseFare) {
        return Arrays.stream(Age.values())
                .filter(type -> type.ageCondition.test(age))
                .map(type -> type.discountPolicy.apply(baseFare))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 연령대가 없습니다."));
    }

    private static int calculateFareForChild(int baseFare) {
        return (int) ((baseFare - BASE_DEDUCTION) * DISCOUNT_RATE_FOR_CHILD);
    }

    private static int calculateFareForTeenager(int baseFare) {
        return (int) ((baseFare - BASE_DEDUCTION) * DISCOUNT_RATE_FOR_TEENAGER);
    }

    private static boolean isBaby(int age) {
        return age < CHILD_AGE;
    }

    private static boolean isChild(int age) {
        return CHILD_AGE <= age
                && age < TEENAGER_AGE;
    }

    private static boolean isTeenager(int age) {
        return TEENAGER_AGE <= age
                && age < ADULT_AGE;
    }

    private static boolean isAdult(int age) {
        return ADULT_AGE <= age;
    }
}
