package wooteco.subway.domain.strategy;

import org.springframework.stereotype.Component;

@Component
public class BasicFareStrategy implements FareStrategy {

    private static final int BASIC_FARE = 1250;
    private static final int NOT_DISCOUNT_FARE = 350;
    private static final int ADDITIONAL_FARE = 100;

    private static final int BASIC_DISTANCE = 10;
    private static final int STEP_ONE_DISTANCE = 50;
    private static final double STEP_ONE_CHARGE_DISTANCE = 5.0;
    private static final double STEP_TWO_CHARGE_DISTANCE = 8.0;

    private static final int BABY_AGE_STANDARD = 6;
    private static final int CHILDREN_AGE_STANDARD = 13;
    private static final int TEENAGER_AGE_STANDARD = 19;

    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;

    @Override

    public int calculateFare(int distance, int extraFare, int age) {
        return applyDiscountByAge(calculateFareByDistance(distance) + extraFare, age);
    }

    private int calculateFareByDistance(int distance) {
        if (distance < BASIC_DISTANCE) {
            return BASIC_FARE;
        }
        if (distance < STEP_ONE_DISTANCE) {
            return BASIC_FARE + calculateStepOne(distance);
        }
        return BASIC_FARE + calculateStepOne(distance) + calculateStepTwo(distance);
    }

    private int calculateStepOne(int distance) {
        return (int) Math.ceil(
                Math.min(distance - BASIC_DISTANCE, STEP_ONE_DISTANCE - BASIC_DISTANCE) / STEP_ONE_CHARGE_DISTANCE)
                * ADDITIONAL_FARE;
    }

    private int calculateStepTwo(int distance) {
        return (int) Math.ceil((distance - STEP_ONE_DISTANCE) / STEP_TWO_CHARGE_DISTANCE) * ADDITIONAL_FARE;
    }

    private int applyDiscountByAge(int fare, int age) {
        if (age < BABY_AGE_STANDARD) {
            return 0;
        }
        if (age < CHILDREN_AGE_STANDARD) {
            return fare - calculateDiscount(fare - NOT_DISCOUNT_FARE, CHILDREN_DISCOUNT_RATE);
        }
        if (age < TEENAGER_AGE_STANDARD) {
            return fare - calculateDiscount(fare - NOT_DISCOUNT_FARE, TEENAGER_DISCOUNT_RATE);
        }
        return fare;
    }

    private int calculateDiscount(int fare, double discountRate) {
        return (int) (fare * discountRate);
    }

}
