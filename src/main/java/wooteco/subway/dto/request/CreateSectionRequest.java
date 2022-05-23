package wooteco.subway.dto.request;

import javax.validation.constraints.NotNull;

public class CreateSectionRequest {

    @NotNull(message = "상행역 ID는 무조건 입력해야 합니다.")
    private Long upStationId;
    @NotNull(message = "하행역 ID는 무조건 입력해야 합니다.")
    private Long downStationId;
    @NotNull(message = "거리는 무조건 입력해야 합니다.")
    private Integer distance;

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
