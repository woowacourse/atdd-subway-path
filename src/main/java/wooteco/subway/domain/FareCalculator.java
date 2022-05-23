package wooteco.subway.domain;

import org.springframework.stereotype.Component;
import wooteco.subway.domain.constant.AgeFarePolicy;
import wooteco.subway.domain.constant.DistanceFarePolicy;

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
                .filter(it -> it.condition().test(age))
                .map(it -> it.calculator().applyAsInt(fare))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("연령을 잘못 입력하였습니다."));
    }

    private Integer findFareByDistance(int distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .filter(it -> it.condition().test(distance))
                .map(it -> it.calculator().applyAsInt(distance))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("거리를 잘못 입력하였습니다."));
    }
}
