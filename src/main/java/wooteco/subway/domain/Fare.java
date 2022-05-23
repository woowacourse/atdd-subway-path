package wooteco.subway.domain;

import static wooteco.subway.domain.Age.*;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int FIRST_SECTION_BASIC_FARE = 800;
    private static final int FIRST_SECTION_CHARGE_UNIT = 5;
    private static final int SECOND_SECTION_CHARGE_UNIT = 8;

    private final int distance;
    private final int extraFare;
    private final Long age;

    public Fare(final int distance, final int extraFare, final Long age) {
        this.distance = distance;
        this.extraFare = extraFare;
        this.age = age;
    }

    public int calculateFare() {
        int fare = calculateFareByDistance() + extraFare;
        if (INFANT.isInclude(age)) {
            return (int) (fare * INFANT.getDiscountRate());
        }
        if (CHILDREN.isInclude(age)) {
            return (int) (fare * CHILDREN.getDiscountRate());
        }
        if (TEENAGER.isInclude(age)) {
            return (int) (fare * TEENAGER.getDiscountRate());
        }
        return fare;
    }

    public int calculateFareByDistance() {
        if (distance < 10) {
            return BASIC_FARE;
        }
        if (distance <= 50) {
            return calcExtraChargeByDistance(distance - 10, FIRST_SECTION_CHARGE_UNIT);
        }
        return FIRST_SECTION_BASIC_FARE + calcExtraChargeByDistance(distance - 50, SECOND_SECTION_CHARGE_UNIT);
    }

    private int calcExtraChargeByDistance(int distance, int unit) {
        int fare = 0;
        fare += (distance / unit) * 100;
        if (distance % unit > 0 || (distance / unit == 0)) {
            fare += 100;
        }
        return BASIC_FARE + fare;
    }
}
