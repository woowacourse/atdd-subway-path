package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Age {

    CHILD(age -> age > 0 && age < 13, 50),
    YOUTH(age -> age >= 13 && age < 19, 20),
    ADULT(age -> age >= 19, 0);

    private static final int EXCLUDED_FARE = 350;
    private static final String NONE_AGE_ERROR = "0세 이하의 나이는 불가능합니다.";

    private final Predicate<Integer> predicate;
    private final int discountRate;

    Age(Predicate<Integer> predicate, int discountRate) {
        this.predicate = predicate;
        this.discountRate = discountRate;
    }

    public static Age valueOf(int age) {
        return Arrays.stream(Age.values())
                .filter(it -> it.predicate.test(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(NONE_AGE_ERROR));
    }

    public int discountFare(int fare) {
        return fare - (fare - EXCLUDED_FARE) * discountRate / 100;
    }
}
