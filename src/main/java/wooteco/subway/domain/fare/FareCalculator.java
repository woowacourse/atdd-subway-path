package wooteco.subway.domain.fare;

import java.util.Arrays;

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
        return Arrays.stream(Distance.values())
                .mapToInt(it -> it.calculateAdditionalFare(distance))
                .sum();
    }
}
