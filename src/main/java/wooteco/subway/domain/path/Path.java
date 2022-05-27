package wooteco.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.Id;
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

    public Fare calculateFare(Age age) {
        long fare = DistanceFarePolicy.calculateByPolicy(distance.getDistance()) + calculateMaximumExtraFare();
        long discountedFare = AgeDiscountPolicy.calculateByPolicy(fare, age);
        return new Fare(discountedFare);
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
