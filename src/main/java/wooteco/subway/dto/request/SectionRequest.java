package wooteco.subway.dto.request;

import javax.validation.constraints.Min;

public class SectionRequest {

    private static final String NUMBER_MIN_RANGE_ERROR = " 1 이상이여야 합니다.";

    @Min(value = 1, message = "상행 종점 아이디는" + NUMBER_MIN_RANGE_ERROR)
    private Long upStationId;

    @Min(value = 1, message = "하행 종점 아이디는" + NUMBER_MIN_RANGE_ERROR)
    private Long downStationId;

    @Min(value = 1, message = "구간 거리는" + NUMBER_MIN_RANGE_ERROR)
    private int distance;

    private SectionRequest() {
    }

    public SectionRequest(final Long upStationId, final Long downStationId, final int distance) {
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
