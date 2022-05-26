package wooteco.subway.domain.fare.agepolicy;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public enum AgePolicy {

    ADULT(age -> age >= 19, (fare, age) -> fare),
    TEENAGER(age -> age >= 13 && age < 19, (fare, age) -> {
        int deductionFare = fare - Constants.DEDUCTIBLE_MONEY;
        return (int) (fare - (deductionFare * Constants.TEENAGER_DISCOUNT_RATE));
    }),
    CHILDREN(age -> age >= 6 && age < 13, (fare, age) -> {
        int deductionFare = fare - Constants.DEDUCTIBLE_MONEY;
        return (int) (fare - (deductionFare * Constants.CHILDREN_DISCOUNT_RATE));
    }),
    BABY(age -> age < 6, (fare, age) -> 0),
    ;

    private static class Constants {
        private static final int DEDUCTIBLE_MONEY = 350;
        private static final double TEENAGER_DISCOUNT_RATE = 0.2;
        private static final double CHILDREN_DISCOUNT_RATE = 0.5;
    }

    private final Predicate<Integer> agePredicate;
    private final BiFunction<Integer, Integer, Integer> calculateFare;

    AgePolicy(final Predicate<Integer> agePredicate, final BiFunction<Integer, Integer, Integer> calculateFare) {
        this.agePredicate = agePredicate;
        this.calculateFare = calculateFare;
    }

    public static int calculateFareByAgePolicy(final int fare, final int age) {
        AgePolicy ageGroup = Arrays.stream(values())
                .filter(value -> value.agePredicate.test(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("나이는 양의 정수로 입력해야합니다."));
        return ageGroup.calculateFare.apply(fare, age);
    }
}
