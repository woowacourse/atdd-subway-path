package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum Age {

    INFANTS(age -> age >= 0 && age < 6, fare -> 0),
    CHILDREN(age -> age >= 6 && age < 13, fare -> (int) ((fare - Discount.FARE) * (1 - Discount.CHILDREN_RATIO))),
    TEENAGERS(age -> age >= 13 && age < 19, fare -> (int) ((fare - Discount.FARE) * (1 - Discount.TEENAGER_RATIO))),
    ADULTS(age -> age >= 19, fare -> fare),
    ;

    private final Predicate<Integer> ageDiscriminator;
    private final UnaryOperator<Integer> fareCalculator;

    Age(final Predicate<Integer> ageDiscriminator, final UnaryOperator<Integer> fareCalculator) {
        this.ageDiscriminator = ageDiscriminator;
        this.fareCalculator = fareCalculator;
    }

    public static Age findAge(final int age) {
        return Arrays.stream(values())
                .filter(ageGroup -> ageGroup.ageDiscriminator.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 연령을 찾을 수 없습니다."));
    }

    public int discountFare(final int fare) {
        return this.fareCalculator.apply(fare);
    }

    private static class Discount {
        private static final int FARE = 350;
        private static final double CHILDREN_RATIO = 0.5;
        private static final double TEENAGER_RATIO = 0.2;
    }
}
