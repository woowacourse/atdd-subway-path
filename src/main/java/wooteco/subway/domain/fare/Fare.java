package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import wooteco.subway.domain.line.LineExtraFare;

public class Fare {

    private static final int BASIC_FARE_VALUE = 1250;
    private static final String EXTRA_FARE_NOT_FOUND_EXCEPTION = "추가 요금 정보가 제공되지 않았습니다.";

    private final int value;

    Fare(int value) {
        this.value = value;
    }

    public Fare(){
        this(BASIC_FARE_VALUE);
    }

    public Fare applyDistanceOverFarePolicies(int distance) {
        int distanceOverFare =  Arrays.stream(DistanceOverFarePolicy.values())
                .filter(policy -> policy.isApplicableTo(distance))
                .mapToInt(policy -> policy.toOverFare(distance))
                .sum();
        return new Fare(value + distanceOverFare);
    }

    public Fare applyMaximumLineExtraFare(List<LineExtraFare> lineExtraFares) {
        int lineExtraFare =  lineExtraFares.stream()
                .mapToInt(LineExtraFare::getValue)
                .max()
                .orElseThrow(() -> new IllegalArgumentException(EXTRA_FARE_NOT_FOUND_EXCEPTION));
        return new Fare(value + lineExtraFare);
    }

    public Fare applyAgeDiscountPolicy(int age) {
        AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.of(age);
        return new Fare(ageDiscountPolicy.applyDiscount(value));
    }

    public int toInt() {
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
