package wooteco.subway;

import static wooteco.subway.Age.Constant.*;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum Age {

    INFANTS(age -> INFANTS_START_AGE <= age && age < CHILDREN_START_AGE, fare -> FARE_FREE),
    CHILDREN(age -> CHILDREN_START_AGE <= age && age < TEENAGERS_START_AGE, fare -> (int) ((fare - FIXED_DISCOUNT_FARE) * CHILDREN_DISCOUNT_RATE)),
    TEENAGERS(age -> TEENAGERS_START_AGE <= age && age < ADULTS_START_AGE, fare -> (int) ((fare - FIXED_DISCOUNT_FARE) * TEENAGERS_DISCOUNT_RATE)),
    ADULTS(age -> ADULTS_START_AGE <= age && age < SENIORS_START_AGE, fare -> fare),
    SENIORS(age -> SENIORS_START_AGE <= age, fare -> FARE_FREE),
    ;

    private final Predicate<Integer> findAgeGroup;
    private final UnaryOperator<Integer> fareCalculator;

    Age(Predicate<Integer> findAgeGroup, UnaryOperator<Integer> fareCalculator) {
        this.findAgeGroup = findAgeGroup;
        this.fareCalculator = fareCalculator;
    }

    public static Age calculateAge(int rawAge) {
        return Arrays.stream(values())
                .filter(age -> age.findAgeGroup.test(rawAge))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 나이입니다."));
    }

    public int calculateDiscount(int fare) {
        return this.fareCalculator.apply(fare);
    }

    static class Constant {

        static final double CHILDREN_DISCOUNT_RATE = 0.5;
        static final double TEENAGERS_DISCOUNT_RATE = 0.8;
        static final int FARE_FREE = 0;
        static final int FIXED_DISCOUNT_FARE = 350;
        static final int INFANTS_START_AGE = 1;
        static final int CHILDREN_START_AGE = 6;
        static final int TEENAGERS_START_AGE = 13;
        static final int ADULTS_START_AGE = 19;
        static final int SENIORS_START_AGE = 65;
    }
}
