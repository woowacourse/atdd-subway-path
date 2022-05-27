package wooteco.subway.dto;

import javax.validation.constraints.NotNull;

public class AddSectionRequest {

    @NotNull(message = "상행역은 빈 값일 수 없습니다.")
    private Long upStationId;

    @NotNull(message = "하행역은 빈 값일 수 없습니다.")
    private Long downStationId;

    @NotNull(message = "거리는 빈 값일 수 없습니다.")
    private Integer distance;

    public AddSectionRequest() {
    }

    public AddSectionRequest(Long upStationId, Long downStationId, Integer distance) {
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

    public Integer getDistance() {
        return distance;
    }
}
