package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgePolicy {
    INFANT(0, 6, 0, 0),
    CHILD(6, 13, 350, 0.5),
    TEEN(13, 19, 350, 0.8),
    ADULT(19, 65, 0, 1),
    OLD(65, Integer.MAX_VALUE, 0, 0);

    private int minAge;
    private int maxAge;
    private int deductibleFare;
    private double fareRatio;

    AgePolicy(int minAge, int maxAge, int deductibleFare, double fareRatio) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.deductibleFare = deductibleFare;
        this.fareRatio = fareRatio;
    }

    public static int calculateFareByAge(int age, int fare) {
        AgePolicy agePolicy = Arrays.stream(values())
                .filter(policy -> age >= policy.minAge && age < policy.maxAge)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 나이입니다."));

        return agePolicy.calculate(fare);
    }

    private int calculate(int fare) {
        return (int) ((fare - deductibleFare) * fareRatio);
    }
}
