package wooteco.subway.domain.fare;

import java.util.List;

public class FareCalculator {

    private final int distance;
    private final int extraFare;
    private final Age age;

    public FareCalculator(int distance, int extraFare, int age) {
        this.distance = distance;
        this.extraFare = extraFare;
        this.age = Age.valueOf(age);
    }

    public int calculate() {
        return age.discountFare(calculateByDistance() + extraFare);
    }

    private int calculateByDistance() {
        List<Distance> distances = Distance.findAvailableDistances(distance);

        return distances.stream()
                .mapToInt(it -> it.calculateAdditionalFare(distance))
                .sum();
    }
}
