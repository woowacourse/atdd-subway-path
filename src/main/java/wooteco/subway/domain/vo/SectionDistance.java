package wooteco.subway.domain.vo;

import java.util.Objects;

public class SectionDistance {

    private final long distance;

    private SectionDistance(Long input) {
        this.distance = validate(input);
    }

    public static SectionDistance from(Long input) {
        return new SectionDistance(input);
    }

    private long validate(Long input) {
        validateNotNull(input);
        validatePositive(input);

        return input;
    }

    private void validateNotNull(Long input) {
        if (Objects.isNull(input)) {
            throw new IllegalArgumentException("구간 사이 거리는 null일 수 없습니다");
        }
    }

    private void validatePositive(long input) {
        if (input < 1) {
            throw new IllegalArgumentException("구간 사이 거리는 1 이상이어야 합니다 : " + input);
        }
    }

    public SectionDistance minus(SectionDistance other) {
        return new SectionDistance(this.distance - other.distance);
    }

    public long getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectionDistance that = (SectionDistance) o;
        return Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
