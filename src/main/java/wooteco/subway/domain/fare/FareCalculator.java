package wooteco.subway.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    public int calculateFare(int distance, int age) {
        int fareByDistance = DistanceFarePolicy.calculateFare(distance);
        int fareByAge = AgeFarePolicy.calculateFare(fareByDistance, age);
        return fareByAge;
    }
}
