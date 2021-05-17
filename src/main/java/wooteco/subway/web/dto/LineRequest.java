package wooteco.subway.web.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LineRequest {
    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;
    @NotEmpty(message = "색을 선택해주세요.")
    private String color;
    @NotNull(message = "상행역을 선택해주세요.")
    private Long upStationId;
    @NotNull(message = "하행역을 선택해주세요.")
    private Long downStationId;
    @Positive(message = "올바르지 않은 거리 형식입니다.")
    private int distance;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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
}
