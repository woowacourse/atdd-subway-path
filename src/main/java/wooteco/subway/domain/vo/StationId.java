package wooteco.subway.domain.vo;

import java.util.Objects;

public class StationId {

    private final Long stationId;

    private StationId(Long input) {
        this.stationId = validate(input);
    }

    public static StationId from(Long input) {
        return new StationId(input);
    }

    private Long validate(Long input) {
        validatePositive(input);

        return input;
    }

    private void validatePositive(Long input) {
        if (Objects.nonNull(input) && input < 1) {
            throw new IllegalArgumentException("지하철역 아이디는 1 이상이어야 합니다 : " + input);
        }
    }

    public Long getStationId() {
        return stationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationId that = (StationId) o;
        return Objects.equals(stationId, that.stationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationId);
    }
}
