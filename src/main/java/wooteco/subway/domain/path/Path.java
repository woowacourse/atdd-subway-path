package wooteco.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.line.Fare;
import wooteco.subway.domain.Id;
import wooteco.subway.domain.line.section.Distance;

public class Path {

    private static final long BASE_FARE = 1250;

    private final List<Id> path;
    private final Distance distance;

    public Path(List<Id> path, Distance distance) {
        this.path = path;
        this.distance = distance;
    }

    public Path(List<Long> path, long distance) {
        this.path = path.stream()
                .map(Id::new)
                .collect(Collectors.toUnmodifiableList());
        this.distance = new Distance(distance);
    }

    public Fare calculateFare() {
        return new Fare(calculateFare(distance.getDistance()));
    }

    private long calculateFare(long distance) {
        if (distance <= 10) {
            return BASE_FARE;
        }

        if (distance <= 50) {
            return BASE_FARE + ((distance - 10 - 1) / 5 + 1) * 100;
        }

        long baseOverFifty = BASE_FARE + 40 / 5 * 100L;
        baseOverFifty += ((distance - 50 - 1) / 8 + 1) * 100L;
        return baseOverFifty;
    }

    public List<Long> getPath() {
        return path.stream()
                .map(Id::getId)
                .collect(Collectors.toUnmodifiableList());
    }

    public long getDistance() {
        return distance.getDistance();
    }
}
