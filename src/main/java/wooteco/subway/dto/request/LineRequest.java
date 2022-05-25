package wooteco.subway.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public class LineRequest {

    @NotNull
    @Size(min = 2, max = 10, message = "노선 이름은 2글자 이상 10글자 이하여야 합니다.")
    private String name;
    @NotNull
    private String color;
    @Positive
    private Long upStationId;
    @Positive
    private Long downStationId;
    @PositiveOrZero(message = "거리는 0 이상의 수만 가능합니다.")
    private int distance;
    @PositiveOrZero(message = "요금은 0 이상의 수만 가능합니다.")
    private int extraFare;

    public LineRequest() {
    }

    public LineRequest(String name, String color) {
        this(name, color, null, null, 0, 0);
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(name, color, upStationId, downStationId, distance, 0);
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId,
            int distance, int extraFare) {
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
