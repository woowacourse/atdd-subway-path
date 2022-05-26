package wooteco.subway.domain;

import wooteco.subway.exception.NoSuchContentException;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgeDiscountPolicy {
    INFANT(age -> 0 <= age && age < 6, 0, 0),
    CHILD(age -> 6 <= age && age < 13, 350, 0.5),
    TEEN(age -> 13 <= age && age < 19, 350, 0.8),
    ADULT(age -> 19 <= age && age < 65, 0, 1),
    OLD(age -> 65 <= age, 0, 0);

    private Predicate<Integer> predicate;
    private int deductibleFare;
    private double fareRatio;

    AgeDiscountPolicy(Predicate<Integer> predicate, int deductibleFare, double fareRatio) {
        this.predicate = predicate;
        this.deductibleFare = deductibleFare;
        this.fareRatio = fareRatio;
    }

    public static int calculateFareByAge(int age, int fare) {
        AgeDiscountPolicy agePolicy = Arrays.stream(values())
                .filter(policy -> policy.predicate.test(age))
                .findFirst()
                .orElseThrow(() -> new NoSuchContentException("입력 나이에 일치하는 값이 존재하지 않습니다."));

        return agePolicy.calculate(fare);
    }

    private int calculate(int fare) {
        return (int) ((fare - deductibleFare) * fareRatio);
    }
}
