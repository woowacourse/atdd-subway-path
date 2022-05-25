package wooteco.subway.ui.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import wooteco.subway.service.dto.LineCreationServiceRequest;

public class LineCreationRequest {

    static final String OMISSION_MESSAGE = "이(가) 유효하지 않습니다.";

    @NotBlank(message = "노선의 이름" + OMISSION_MESSAGE)
    private String name;
    @NotBlank(message = "노선의 색상" + OMISSION_MESSAGE)
    private String color;
    @NotNull(message = "노선의 상행역" + OMISSION_MESSAGE)
    private Long upStationId;
    @NotNull(message = "노선의 하행역" + OMISSION_MESSAGE)
    private Long downStationId;
    @NotNull(message = "노선의 거리 값" + OMISSION_MESSAGE)
    private Integer distance;
    @NotNull(message = "추가 요금 값" + OMISSION_MESSAGE)
    private Integer extraFare;

    public LineCreationRequest() {
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
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

    public Integer getExtraFare() {
        return extraFare;
    }

    public LineCreationServiceRequest toServiceRequest() {
        return new LineCreationServiceRequest(name, color, upStationId, downStationId, distance, extraFare);
    }
}
