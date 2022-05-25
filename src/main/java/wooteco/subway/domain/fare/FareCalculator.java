package wooteco.subway.domain.fare;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class FareCalculator {

    public int calculateFare(int distance, int age) {
        Integer fareByDistance = findFareByDistance(distance);
        Integer fareByAge = findFareByAge(fareByDistance, age);
        return fareByAge;
    }
    private Integer findFareByAge(Integer fare, int age) {
        return Arrays.stream(AgeFarePolicy.values())
                .filter(AgeFarePolicy -> AgeFarePolicy.condition().test(age))
                .map(AgeFarePolicy -> AgeFarePolicy.calculator().applyAsInt(fare))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("연령을 잘못 입력하였습니다."));
    }

    private Integer findFareByDistance(int distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .filter(distanceFarePolicy -> distanceFarePolicy.condition().test(distance))
                .map(distanceFarePolicy -> distanceFarePolicy.calculator().applyAsInt(distance))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("거리를 잘못 입력하였습니다."));
    }
}
