package wooteco.subway;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum Age {

    INFANTS(age -> 1 <= age && age < 6, fare -> 0),
    CHILDREN(age -> 6 <= age && age < 13, fare -> (int) ((fare - 350) * 0.5)),
    TEENAGERS(age -> 13 <= age && age <19, fare -> (int) ((fare - 350) * 0.8)),
    ADULTS(age -> 19 <= age && age < 65, fare -> fare),
    SENIORS(age -> 65 <= age, fare -> 0),
    ;

    private final Predicate<Integer> findAgeGroup;
    private final Function<Integer, Integer> fareCalculator;

    Age(Predicate<Integer> findAgeGroup, Function<Integer, Integer> fareCalculator) {
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
}
