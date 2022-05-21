package wooteco.subway.dto;

import wooteco.subway.exception.PositiveDigitException;

public class SectionRequest {

    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
        validatePositiveDistance(distance);
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    private void validatePositiveDistance(int distance) {
        if (distance <= 0) {
            throw new PositiveDigitException("구간의 길이가 양수가 아닙니다.");
        }
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
