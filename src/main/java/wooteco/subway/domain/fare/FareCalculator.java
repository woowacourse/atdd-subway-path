package wooteco.subway.domain.fare;

import java.util.Arrays;

public class FareCalculator {

    private static final String INVALID_DISTANCE_ERROR = "0 이하의 거리는 존재하지 않습니다.";
    private static final int MIN_DISTANCE = 1;

    private final int distance;
    private final int extraFare;
    private final Age age;

    public FareCalculator(int distance, int extraFare, int age) {
        validDistance(distance);
        this.distance = distance;
        this.extraFare = extraFare;
        this.age = Age.valueOf(age);
    }

    private void validDistance(int distance) {
        if (distance < MIN_DISTANCE) {
            throw new IllegalArgumentException(INVALID_DISTANCE_ERROR);
        }
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
