package wooteco.subway.domain;

import wooteco.subway.exception.NotFoundException;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgeDiscountPolicy {

    PRESCHOOLER(AgeDiscountPolicy::isPreschooler, fare -> 0),
    CHILD(AgeDiscountPolicy::isChild, AgeDiscountPolicy::calculateChildDiscount),
    TEENAGER(AgeDiscountPolicy::isTeenager, AgeDiscountPolicy::calculateTeenagerDiscount),
    ADULT(AgeDiscountPolicy::isAdult, fare -> fare);

    private final Predicate<Integer> isMatchAge;
    private final Function<Integer, Integer> discountCalculation;

    AgeDiscountPolicy(Predicate<Integer> isMatchAge, Function<Integer, Integer> discountCalculation) {
        this.isMatchAge = isMatchAge;
        this.discountCalculation = discountCalculation;
    }

    private static boolean isPreschooler(int age) {
        return age < 6;
    }

    private static boolean isChild(int age) {
        return 6 <= age && age < 13;
    }

    private static boolean isTeenager(int age) {
        return 13 <= age && age < 19;
    }

    private static boolean isAdult(int age) {
        return 19 <= age;
    }

    private static int calculateChildDiscount(int fare) {
        return (int) ((fare - 350) * 0.5);
    }

    private static int calculateTeenagerDiscount(int fare) {
        return (int) ((fare - 350) * 0.8);
    }

    public static AgeDiscountPolicy findAgePolicy(int age) {
        return Arrays.stream(values())
                .filter(v -> v.isMatchAge.test(age))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("해당하는 연령대를 찾지 못하였습니다."));
    }

    public int fareDiscount(int fare) {
        return discountCalculation.apply(fare);
    }
}
