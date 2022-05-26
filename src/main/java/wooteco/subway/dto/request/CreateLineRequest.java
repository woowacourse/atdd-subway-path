package wooteco.subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import wooteco.subway.domain.Line;

public class CreateLineRequest {

    @NotBlank(message = "이름은 무조건 입력해야 합니다.")
    private String name;
    @NotBlank(message = "색상은 무조건 입력해야 합니다.")
    private String color;
    @NotNull(message = "상행역 ID는 무조건 입력해야 합니다.")
    private Long upStationId;
    @NotNull(message = "하행역 ID는 무조건 입력해야 합니다.")
    private Long downStationId;
    @NotNull(message = "거리는 무조건 입력해야 합니다.")
    private Integer distance;
    @NotNull(message = "추가 금액은 무조건 입력해야 합니다.")
    private Integer extraFare;

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
