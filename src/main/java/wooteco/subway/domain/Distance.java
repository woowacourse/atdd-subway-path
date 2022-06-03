package wooteco.subway.domain;

import java.util.Objects;

public class Distance {

    private final int distance;

    private static final int MINIMUM_DISTANCE = 0;

    public Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(final int distance) {
        if (distance <= MINIMUM_DISTANCE) {
            throw new IllegalArgumentException("거리는 0 또는 음수 일 수 없습니다.");
        }
    }

    public Distance subtract(final Distance target) {
        return new Distance(distance - target.distance);
    }

    public Distance sum(Distance target) {
        return new Distance(distance + target.distance);
    }

    public int getValue() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
