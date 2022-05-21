package wooteco.subway.dto.line;

import javax.validation.constraints.NotBlank;

public class LineRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String color;

    private long upStationId;

    private long downStationId;

    private int distance;

    private int extraFare;

    private LineRequest() {
    }

    public LineRequest(String name, String color, long upStationId, long downStationId, int distance, int extraFare) {
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
