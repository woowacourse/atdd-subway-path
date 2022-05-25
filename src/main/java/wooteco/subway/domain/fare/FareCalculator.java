package wooteco.subway.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    private final int basicFare = 1250;
    private final int firstCheckDistance = 10;
    private final int secondCheckDistance = 50;
    private final double firstCheckDivide = 5.0;
    private final double secondCheckDivide = 8.0;
    private final int additionalFare = 100;

    public int makeFare(int totalDistance, int maxExtraFare, int age) {
        int calculatedFairByDistance = calculateByDistance(basicFare, totalDistance);
        int calculatedFairByMaxExtraFare = calculatedFairByDistance + maxExtraFare;
        return FareByAge.calculateFare(age, calculatedFairByMaxExtraFare);
    }

    private int calculateByDistance(int fare, int distance) {
        if (distance <= firstCheckDistance) {
            return fare;
        }
        if (distance <= secondCheckDistance) {
            return fare + calculateOverTenDistance(distance);
        }
        return fare + calculateOverTenDistance(secondCheckDistance) + calculateOverFiftyDistance(distance);
    }

    private int calculateOverTenDistance(int distance) {
        return (int) ((Math.ceil((distance - firstCheckDistance) / firstCheckDivide)) * additionalFare);
    }

    private int calculateOverFiftyDistance(int distance) {
        return (int) ((Math.ceil((distance - secondCheckDistance) / secondCheckDivide)) * additionalFare);
    }
}
