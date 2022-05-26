package wooteco.subway.domain.path;

import java.util.Collections;
import java.util.Set;

public class Path {
    private final int distance;
    private final Set<Long> lineIds;
    private final Set<Long> stationIds;

    public Path(int distance, Set<Long> lineIds, Set<Long> stationIds) {
        this.distance = distance;
        this.lineIds = lineIds;
        this.stationIds = stationIds;
    }

    public int calculateFare(int age, int extraCharge) {
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.of(distance);
        int fare = distanceFarePolicy.calculate(distance) + extraCharge;

        AgeDiscountPolicy discount = AgeDiscountPolicy.of(age);
        return discount.apply(fare);
    }

    public int getDistance() {
        return distance;
    }

    public Set<Long> getLineIds() {
        return Collections.unmodifiableSet(lineIds);
    }

    public Set<Long> getStationIds() {
        return Collections.unmodifiableSet(stationIds);
    }
}
