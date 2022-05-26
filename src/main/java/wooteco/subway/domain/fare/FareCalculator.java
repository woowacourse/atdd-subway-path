package wooteco.subway.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    private static final int BASIC_FARE = 1250;
    private static final int FIRST_CHECK_DISTANCE = 10;
    private static final int SECOND_CHECK_DISTANCE = 50;
    private static final double FIRST_CHECK_DIVIDE = 5.0;
    private static final double SECOND_CHECK_DIVIDE = 8.0;
    private static final int ADDITIONAL_FARE = 100;

    public int makeFare(int totalDistance, int maxExtraFare, int age) {
        int calculatedFairByDistance = calculateByDistance(BASIC_FARE, totalDistance);
        int calculatedFairByMaxExtraFare = calculatedFairByDistance + maxExtraFare;
        return FareByAge.calculateFare(age, calculatedFairByMaxExtraFare);
    }

    private int calculateByDistance(int fare, int distance) {
        if (distance <= FIRST_CHECK_DISTANCE) {
            return fare;
        }
        if (distance <= SECOND_CHECK_DISTANCE) {
            return fare + calculateOverTenDistance(distance);
        }
        return fare + calculateOverTenDistance(SECOND_CHECK_DISTANCE) + calculateOverFiftyDistance(distance);
    }

    private int calculateOverTenDistance(int distance) {
        return (int) ((Math.ceil((distance - FIRST_CHECK_DISTANCE) / FIRST_CHECK_DIVIDE)) * ADDITIONAL_FARE);
    }

    private int calculateOverFiftyDistance(int distance) {
        return (int) ((Math.ceil((distance - SECOND_CHECK_DISTANCE) / SECOND_CHECK_DIVIDE)) * ADDITIONAL_FARE);
    }
}
