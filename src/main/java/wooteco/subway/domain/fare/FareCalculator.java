package wooteco.subway.domain.fare;

import java.util.List;

public class FareCalculator {

    private final int distance;
    private final int extraFare;
    private final Age age;

    public FareCalculator(int distance, int extraFare, int age) {
        this.distance = distance;
        this.extraFare = extraFare;
        this.age = Age.findByAge(age);
    }

    public int calculate() {
        return age.calculateFare(calculateByDistance() + extraFare);
    }

    private int calculateByDistance() {
        int distanceForPay = this.distance;
        List<Distance> distances = Distance.findByDistance(distanceForPay);
        int sum = 0;

        for (Distance distance : distances) {
            sum += distance.calculateAdditionalFare(distanceForPay);
            distanceForPay = distance.getDistanceUnit();
        }
        return sum;
    }
}
