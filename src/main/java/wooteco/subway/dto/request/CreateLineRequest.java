package wooteco.subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import wooteco.subway.domain.Line;

public class CreateLineRequest {

    @NotBlank(message = "노선 이름은 공백일 수 없습니다.")
    private String name;
    @NotBlank(message = "색상이 공백일 수 없습니다.")
    private String color;
    private Long upStationId;
    private Long downStationId;
    @Positive(message = "거리는 0보다 커야합니다.")
    private int distance;
    @PositiveOrZero(message = "추가 요금은 0원 이상이어야 합니다.")
    private int extraFare;

    private CreateLineRequest() {
    }

    public CreateLineRequest(final String name, final String color, final Long upStationId, final Long downStationId,
                             final int distance, final int extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public Line toLine() {
        return new Line(name, color, extraFare);
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
