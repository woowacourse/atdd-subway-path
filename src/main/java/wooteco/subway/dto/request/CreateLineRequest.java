package wooteco.subway.dto.request;

import javax.validation.constraints.NotBlank;
import wooteco.subway.domain.Line;

public class CreateLineRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
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
