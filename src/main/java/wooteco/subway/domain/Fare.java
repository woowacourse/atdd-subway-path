package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;

public class Fare {
    private static final int DISCOUNT_EXCEPT_MONEY_UNIT = 350;

    private final int value;

    private Fare(int value) {
        this.value = value;
    }

    public static Fare of(int distance, List<Line> passedLines, int age) {
        int defaultFare = calculateDefaultFare(distance);
        double discountRate = calculateDiscountRate(age);

        int extraFare = calculateExtraFare(passedLines);

        int fare = defaultFare + extraFare;
        int discount = (int) ((fare - DISCOUNT_EXCEPT_MONEY_UNIT) * discountRate);

        return new Fare(fare - discount);
    }

    private static int calculateDefaultFare(int distance) {
        DistanceFare distanceFare = DistanceFare.from(distance);
        return distanceFare.calculateFare(distance);
    }

    private static double calculateDiscountRate(int age) {
        DiscountRatesAge discountRatesAge = DiscountRatesAge.from(age);
        return discountRatesAge.getDiscountRate();
    }

    private static int calculateExtraFare(List<Line> passedLines) {
        return passedLines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("추가 운임 계산 중 에러가 발생하였습니다."));
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
