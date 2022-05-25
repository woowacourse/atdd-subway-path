package wooteco.subway.dto.line;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LineRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String color;

    @NotNull
    private Long upStationId;

    @NotNull
    private Long downStationId;

    @Positive
    private int distance;

    private int extraFare;

    private LineRequest() {
    }

    public LineRequest(final String name, final String color, final Long upStationId, final Long downStationId,
                       final int distance, final int extraFare) {
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
