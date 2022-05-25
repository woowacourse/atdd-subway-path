package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DiscountTable {

    TEENAGER(value -> (int) ((value - Constants.DEDUCTION_PRICE) * Constants.TEENAGER_DISCOUNT_RATE),
            age -> age >= Constants.TEENAGER_MINIMUM_AGE
                    && age < Constants.ADULT_AGE),
    CHILD(value -> (int) ((value - Constants.DEDUCTION_PRICE) * Constants.CHILD_DISCOUNT_RATE),
            age -> age >= Constants.CHILD_MINIMUM_AGE
                    && age < Constants.TEENAGER_MINIMUM_AGE),
    NONE(value -> value, age -> age < Constants.CHILD_MINIMUM_AGE || age >= Constants.ADULT_AGE);

    private final Function<Integer, Integer> expression;
    private final Predicate<Integer> predicate;

    DiscountTable(final Function<Integer, Integer> expression, final Predicate<Integer> predicate) {
        this.expression = expression;
        this.predicate = predicate;
    }

    public static int calculateFareWithDiscount(int fare, int age) {
        return Arrays.stream(DiscountTable.values())
                .filter(discountTable -> discountTable.predicate.test(age))
                .findAny()
                .map(discountTable -> discountTable.expression.apply(fare))
                .orElse(fare);
    }

    private static class Constants {
        private static final int DEDUCTION_PRICE = 350;
        private static final double TEENAGER_DISCOUNT_RATE = 0.8;
        private static final int TEENAGER_MINIMUM_AGE = 13;
        private static final int ADULT_AGE = 19;
        private static final double CHILD_DISCOUNT_RATE = 0.5;
        private static final int CHILD_MINIMUM_AGE = 6;
    }
}
