package wooteco.subway.util;

import wooteco.subway.domain.property.Age;
import wooteco.subway.domain.property.Distance;

public class FareCalculator {

    private static final int BASIC_FARE = 1250;
    private static final int BABY_FARE = 0;
    private static final int EXTRA_FARE = 100;

    private static final int BASE_DISTANCE_THRESHOLD = 10;
    private static final int EXTRA_FARE_DISTANCE_THRESHOLD = 50;

    private static final int LOW_EXTRA_UNIT = 5;
    private static final int HIGH_EXTRA_UNIT = 8;

    private static final int DISCOUNT_RATIO_OF_TEENAGER = 20;
    private static final int DISCOUNT_RATIO_OF_CHILDREN = 50;
    private static final int DISCOUNT_DEDUCTION = 350;

    public static int calculate(Distance distance, Age age, int extraFare) {
        if (age.isBaby()) {
            return BABY_FARE;
        }

        int amount = BASIC_FARE;
        amount += calculateBasicExtra(distance);
        amount += calculateHighExtra(distance);
        amount -= calculateDiscount(amount, age);
        amount += extraFare;

        return amount;
    }

    private static int calculateBasicExtra(Distance distance) {
        return Math.min(
                calculateExtraFare(distance, BASE_DISTANCE_THRESHOLD, LOW_EXTRA_UNIT),
                HIGH_EXTRA_UNIT * EXTRA_FARE
        );
    }

    private static int calculateHighExtra(Distance distance) {
        return calculateExtraFare(distance, EXTRA_FARE_DISTANCE_THRESHOLD, HIGH_EXTRA_UNIT);
    }

    private static int calculateDiscount(int amount, Age age) {
        if (age.isTeenager()) {
            return calculateDiscountFare(amount, DISCOUNT_RATIO_OF_TEENAGER);
        }
        if (age.isChildren()) {
            return calculateDiscountFare(amount, DISCOUNT_RATIO_OF_CHILDREN);
        }
        return 0;
    }

    private static int calculateExtraFare(Distance distance, int threshold, int unit) {
        if (distance.isShorterThan(threshold)) {
            return 0;
        }
        return distance.minusAndDivide(threshold, unit) * EXTRA_FARE;
    }

    private static int calculateDiscountFare(int amount, int ratio) {
        return (amount - DISCOUNT_DEDUCTION) * ratio / 100;
    }

}
