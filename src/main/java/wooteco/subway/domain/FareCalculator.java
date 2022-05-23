package wooteco.subway.domain;

import java.util.List;

public class FareCalculator {

    private static final int DEFAULT_FARE = 1250;
    private static final int SURCHARGE_PER_UNIT = 100;
    private static final int FIRST_STANDARD = 10;
    private static final int SECOND_STANDARD = 50;
    private static final int FIRST_STANDARD_UNIT = 5;
    private static final int SECOND_STANDARD_UNIT = 8;
    private static final int SURCHARGE_UNIT_COUNT = 8;
    private static final int DEFAULT_VALUE = 0;

    public static int calculate(int distance, List<Integer> extraFares, int age) {
        int fare = calculateByDistance(distance) + calculateExtraFare(extraFares);
        return calculateByAge(fare, age);
    }

    public static int calculateByDistance(int distance) {
        if (distance == 0) {
            return 0;
        }

        if (distance > SECOND_STANDARD) {
            return DEFAULT_FARE + SURCHARGE_PER_UNIT * SURCHARGE_UNIT_COUNT + (int) (
                    ((Math.ceil((distance - SECOND_STANDARD) / (double) SECOND_STANDARD_UNIT))) * SURCHARGE_PER_UNIT);
        }
        if (distance > FIRST_STANDARD) {
            return DEFAULT_FARE + (int) ((Math.ceil((distance - FIRST_STANDARD) / (double) FIRST_STANDARD_UNIT))
                    * SURCHARGE_PER_UNIT);
        }
        return DEFAULT_FARE;
    }

    private static int calculateExtraFare(List<Integer> extraFares) {
        return extraFares.stream()
                .mapToInt(extraFare -> extraFare)
                .max()
                .orElse(DEFAULT_VALUE);
    }

    private static int calculateByAge(int fare, int age) {
        if (age >= 13 && age < 19) {
            return (int) ((fare - 350) * 0.8);
        }
        if (age >= 6 && age < 13) {
            return (int) ((fare - 350) * 0.5);
        }
        return fare;
    }
}
