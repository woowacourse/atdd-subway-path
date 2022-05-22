package wooteco.subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class LineCreateRequest {

    @NotBlank(message = "노선 이름은 빈 값일 수 없습니다.")
    private final String name;
    @NotBlank(message = "노선 색상은 빈 값일 수 없습니다.")
    private final String color;
    @Positive(message = "시점의 id는 1보다 작을 수 없습니다.")
    @NotNull(message = "시점의 id는 null일 수 없습니다.")
    private final Long upStationId;
    @Positive(message = "종점의 id는 1보다 작을 수 없습니다.")
    @NotNull(message = "종점의 id는 null일 수 없습니다.")
    private final Long downStationId;
    @Positive(message = "노선 거리는 1보다 작을 수 없습니다.")
    private final int distance;
    @PositiveOrZero(message = "추가 요금은 음수일 수 없습니다.")
    private final int extraFare;

    private LineCreateRequest() {
        this(null, null, null, null, 0, 0);
    }

    public LineCreateRequest(String name, String color, Long upStationId, Long downStationId, int distance,
                             int extraFare) {
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