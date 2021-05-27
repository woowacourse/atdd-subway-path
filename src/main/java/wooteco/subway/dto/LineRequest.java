package wooteco.subway.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class LineRequest {
    @NotNull
    private String name;
    @NotNull
    private String color;
    @NotNull
    private Long upStationId;
    @NotNull
    private Long downStationId;
    @NotNull
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
}
