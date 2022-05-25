package wooteco.subway.dto.request;

import javax.validation.constraints.Positive;

public class CreateSectionRequest {

    private Long upStationId;
    private Long downStationId;
    @Positive(message = "거리는 0보다 커야합니다.")
    private int distance;

    private CreateSectionRequest() {
    }

    public CreateSectionRequest(final Long upStationId, final Long downStationId, final int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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
