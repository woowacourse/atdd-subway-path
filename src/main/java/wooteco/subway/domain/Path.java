package wooteco.subway.domain;

import java.util.List;
import wooteco.subway.strategy.DistanceFareStrategy;

public class Path {

    private final List<Station> stations;
    private final Lines visitLines;
    private final int distance;

    public Path(List<Station> stations, Lines visitLines, int distance) {
        this.stations = stations;
        this.visitLines = visitLines;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare(int age) {
        int fare = new DistanceFareStrategy().calculate(distance) + visitLines.getMaxExtraFare();
        AgePolicy agePolicy = AgePolicy.fromAge(age);
        return agePolicy.getDiscountedFare(fare);
    }
}
