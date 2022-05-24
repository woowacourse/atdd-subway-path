package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DiscountPolicy {
    INFANT(age -> age < 6, fare -> 0),
    CHILD(age -> age >= 6 && age <= 12, fare -> (int) ((fare - 350) * 0.5)),
    TEEN(age -> age >= 13 && age <= 18, fare -> (int) ((fare - 350) * 0.8)),
    SENIOR(age -> age >= 65, fare -> 0),
    ADULT(age -> age >= 19 && age < 65, fare -> fare);

    private final Predicate<Integer> predicate;
    private final Function<Integer, Integer> discountedFare;

    DiscountPolicy(Predicate<Integer> predicate, Function<Integer, Integer> discountedFare) {
        this.predicate = predicate;
        this.discountedFare = discountedFare;
    }

    public static int calculateDiscountedFare(int age, int fare) {
        return Arrays.stream(values())
                .filter(discountPolicy -> discountPolicy.predicate.test(age))
                .map(discountPolicy -> discountPolicy.discountedFare.apply(fare))
                .findFirst()
                .orElseThrow();
    }
}
