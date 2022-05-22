package wooteco.subway.ui.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import wooteco.subway.service.dto.LineServiceRequest;

public class LineRequest {

    @NotBlank(message = "노선 이름이 필요합니다.")
    private String name;

    @NotBlank(message = "노선 색상이 필요합니다.")
    private String color;

    @NotNull(message = "상행종점 id기 필요합니다.")
    private Long upStationId;

    @NotNull(message = "하행종점 id기 필요합니다.")
    private Long downStationId;

    @Positive(message = "거리는 1 이상이어야 합니다.")
    private int distance;

    @PositiveOrZero(message = "추가요금은 0원 이상이어야 합니다.")
    private int extraFare;

    private LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
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

    public int getDistance() {
        return distance;
    }

    public int getExtraFare() {
        return extraFare;
    }

    public LineServiceRequest toServiceRequest() {
        return new LineServiceRequest(name, color, upStationId, downStationId, distance, extraFare);
    }
}
