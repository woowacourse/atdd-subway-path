package wooteco.subway.domain.path.cost;

public class CostManager {

    public int calculateFare(int totalDistance, int extraFare, int age) {
        return AgeSection.calculateByAge(
                DistanceSection.calculateByDistance(totalDistance) + extraFare, age);
    }
}
