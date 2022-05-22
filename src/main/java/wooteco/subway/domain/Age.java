package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum Age {

    BABIES(age -> age >= 0 && age < 6, fare -> 0),
    CHILDREN(age -> age >= 6 && age < 13, fare -> (int) ((fare - 350) * (1 - 0.5))),
    TEENAGERS(age -> age >= 13 && age < 19, fare -> (int) ((fare - 350) * (1 - 0.2))),
    ADULTS(age -> age >= 19, fare -> fare),
    ;

    private final Predicate<Integer> ageDiscriminator;
    private final Function<Integer, Integer> fareCalculator;

    Age(final Predicate<Integer> ageDiscriminator, Function<Integer, Integer> fareCalculator) {
        this.ageDiscriminator = ageDiscriminator;
        this.fareCalculator = fareCalculator;
    }

    public static int discountFare(int age, int fare) {
        return Arrays.stream(values())
                .filter(ageGroup -> ageGroup.ageDiscriminator.test(age))
                .map(ageGroup -> ageGroup.fareCalculator.apply(fare))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("올바른 연령 값이 아닙니다."));
    }
}
