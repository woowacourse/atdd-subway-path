package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgePolicy {

    BABY(AgePolicy::isBaby, AgePolicy::getFareByBaby),
    CHILDREN(AgePolicy::isChildren, AgePolicy::getFareByChildren),
    TEENAGER(AgePolicy::isTeenager, AgePolicy::getFareByTeenager),
    ADULT(AgePolicy::isAdult, AgePolicy::getFareByAdult);

    private final Predicate<Integer> ageGroup;
    private final Function<Integer, Integer> discountPolicy;

    AgePolicy(Predicate<Integer> ageGroup, Function<Integer, Integer> discountPolicy) {
        this.ageGroup = ageGroup;
        this.discountPolicy = discountPolicy;
    }

    public static AgePolicy fromAge(int age) {
        validatePositive(age);
        return Arrays.stream(AgePolicy.values())
                .filter(agePolicy -> agePolicy.ageGroup.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 나이입니다."));
    }

    private static void validatePositive(int age) {
        if (age <= 0) {
            throw new IllegalArgumentException("나이는 1이상이어야 합니다.");
        }
    }

    public int getDiscountedFare(int fare) {
        return discountPolicy.apply(fare);
    }

    private static boolean isBaby(Integer age) {
        return age <= 5;
    }

    private static boolean isChildren(Integer age) {
        return 6 <= age && age <= 12;
    }

    private static boolean isTeenager(Integer age) {
        return 13 <= age && age <= 18;
    }

    private static boolean isAdult(Integer age) {
        return 19 <= age;
    }

    private static int getFareByBaby(Integer fare) {
        return 0;
    }

    private static int getFareByChildren(Integer fare) {
        return (int) ((fare - 350) * 0.5);
    }

    private static Integer getFareByTeenager(Integer fare) {
        return (int) ((fare - 350) * 0.8);
    }

    private static Integer getFareByAdult(Integer fare) {
        return fare;
    }
}
