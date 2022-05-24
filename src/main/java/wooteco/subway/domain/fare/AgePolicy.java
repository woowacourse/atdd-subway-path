package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgePolicy {

    ADULT(age -> age >= 19 || age < 6, 0),
    TEENAGER(age -> age >= 13 && age < 19, 0.2),
    CHILDREN(age -> age >= 6 && age < 13, 0.5),
    ;

    private static final int DEDUCTIBLE_MONEY = 350;

    private final Predicate<Integer> predicate;
    private final double discountRate;

    AgePolicy(final Predicate<Integer> predicate, final double discountRate) {
        this.predicate = predicate;
        this.discountRate = discountRate;
    }

    public static int calculateFareByAgePolicy(final int fare, final int age) {
        AgePolicy ageGroup = Arrays.stream(values())
                .filter(value -> value.predicate.test(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("나이는 양의 정수로 입력해야합니다."));
        return ageGroup.calculateFare(fare);
    }

    private int calculateFare(final int totalFare) {
        int deductionFare = totalFare - DEDUCTIBLE_MONEY;
        return (int) (totalFare - (deductionFare * discountRate));
    }
}
