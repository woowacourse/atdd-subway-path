package wooteco.subway.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public class LineRequest {

    @NotEmpty(message = "노선의 이름은 공백일 수 없습니다.")
    @Size(max = 255, message = "노선의 이름은 255자 이하여야합니다.")
    private String name;

    @NotEmpty(message = "노선의 색은 공백일 수 없습니다.")
    @Size(max = 20, message = "노선의 색은 20자 이하여야합니다.")
    private String color;
    private Long upStationId;
    private Long downStationId;

    @Positive(message = "거리는 양수여야합니다.")
    private int distance;

    @PositiveOrZero(message = "추가 요금은 0원 이상이어야합니다.")
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
