package wooteco.subway.presentation.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String color;

    @NotNull
    @Min(0)
    private Long upStationId;

    @NotNull
    @Min(0)
    private Long downStationId;

    @NotNull
    @Min(1)
    private Integer distance;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, Integer distance) {
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

    public Integer getDistance() {
        return distance;
    }
}
