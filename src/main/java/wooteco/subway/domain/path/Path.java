package wooteco.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.Id;
import wooteco.subway.domain.line.Fare;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.section.Distance;

public class Path {

    private static final long BASE_FARE = 1250;

    private final List<Id> path;
    private final List<Line> passedLines;
    private final Distance distance;

    public Path(List<Id> path, List<Line> passedLines, Distance distance) {
        this.path = path;
        this.passedLines = passedLines;
        this.distance = distance;
    }

    public Path(List<Id> path, List<Line> passedLines, long distance) {
        this(path, passedLines, new Distance(distance));
    }

    public Fare calculateFare() {
        long fare = calculateFareByDistance(distance.getDistance());
        fare += calculateMaximumExtraFare();
        return new Fare(fare);
    }

    private long calculateFareByDistance(long distance) {
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

    private long calculateMaximumExtraFare() {
        return passedLines.stream()
                .mapToLong(Line::getExtraFare)
                .max()
                .orElse(0);
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
