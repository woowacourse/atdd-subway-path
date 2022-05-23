package wooteco.subway.dto;

import wooteco.subway.domain.line.LineEntity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LineRequest {

    @NotNull
    private String name;

    @NotNull
    @Size(max = 20)
    private String color;

    @NotNull
    private Long upStationId;

    @NotNull
    private Long downStationId;

    @Min(0)
    private int distance;

    @Min(0)
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

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(name, color, upStationId, downStationId, distance, 0);
    }

    public LineEntity toLineEntity() {
        return new LineEntity(name, color, extraFare);
    }

    public LineEntity toLineInfoWithId(Long id) {
        return new LineEntity(id, name, color, extraFare);
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
