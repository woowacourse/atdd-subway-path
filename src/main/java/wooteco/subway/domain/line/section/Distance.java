package wooteco.subway.domain.line.section;

public class Distance {

    private final long distance;

    public Distance(long distance) {
        validateDistancePositive(distance);
        this.distance = distance;
    }

    private void validateDistancePositive(long distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 양수여야 합니다.");
        }
    }

    public boolean isLongerThan(Distance other) {
        return this.distance > other.distance;
    }

    public Distance subtract(Distance other) {
        return new Distance(Math.abs(this.distance - other.distance));
    }

    public Distance sum(Distance other) {
        return new Distance(this.distance + other.distance);
    }

    public long getDistance() {
        return distance;
    }
}
