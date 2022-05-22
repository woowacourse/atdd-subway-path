package wooteco.subway.domain.path;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import wooteco.subway.application.exception.RidiculousAgeException;

public enum AgeGroup {
    INFANTS(age -> 0 < age && age < 6, fare -> 0),
    CHILDREN(age -> 6 <= age && age < 13, fare -> (int) ((fare - 350) * 0.5)),
    TEENAGERS(age -> 13 <= age && age < 19, fare -> (int) ((fare - 350) * 0.8)),
    ADULTS(age -> 19 <= age && age < 65, fare -> fare),
    SENIORS(age -> 65 <= age && age <= 150, fare -> 0);

    private final Predicate<Integer> findAgeGroup;
    private final Function<Integer, Integer> fareCalculator;

    AgeGroup(Predicate<Integer> findAgeGroup, Function<Integer, Integer> fareCalculator) {
        this.findAgeGroup = findAgeGroup;
        this.fareCalculator = fareCalculator;
    }

    public static int discount(int age, int fare) {
        return findAgeGroup(age)
                .fareCalculator.apply(fare);
    }

    private static AgeGroup findAgeGroup(int age) {
        return Arrays.stream(values())
                .filter(ageGroupGroup -> ageGroupGroup.findAgeGroup.test(age))
                .findFirst()
                .orElseThrow(RidiculousAgeException::new);
    }
}
