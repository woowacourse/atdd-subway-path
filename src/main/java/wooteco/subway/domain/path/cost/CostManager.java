package wooteco.subway.domain.path.cost;

import org.springframework.stereotype.Component;

@Component
public class CostManager {

    public int calculateFare(int totalDistance, int extraFare, int age) {
        return AgeSection.calculateByAge(
                DistanceSection.calculateByDistance(totalDistance) + extraFare, age);
    }
}
