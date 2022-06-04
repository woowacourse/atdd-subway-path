package wooteco.subway.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineRequest {
    @NotBlank(message = "노선 이름은 필수입니다.")
    private String name;
    @NotBlank(message = "노선 색은 필수입니다.")
    private String color;
    @NotNull(message = "상행 종점역은 필수입니다.")
    private Long upStationId;
    @NotNull(message = "하행 종점역은 필수입니다.")
    private Long downStationId;
    @NotNull(message = "거리는 필수입니다.")
    @Min(value = 0, message = "거리는 음수일 수 없습니다.")
    private int distance;
    @Min(value = 0, message = "추가요금은 음수일 수 없습니다.")
    private int extraFare;

    public LineRequest() {
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
}
