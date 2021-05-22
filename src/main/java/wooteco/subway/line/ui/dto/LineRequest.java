package wooteco.subway.line.ui.dto;

import wooteco.subway.auth.ui.dto.valid.NumberValidation;
import wooteco.subway.auth.ui.dto.valid.StringValidation;

import java.beans.ConstructorProperties;

public class LineRequest {

    @StringValidation
    private final String name;
    @StringValidation
    private final String color;
    @NumberValidation
    private final Long upStationId;
    @NumberValidation
    private final Long downStationId;
    @NumberValidation
    private final int distance;

    @ConstructorProperties({"name", "color", "upStationId", "downStationId", "distance"})
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
