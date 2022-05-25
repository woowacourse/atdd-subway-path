package wooteco.subway.dto.controller.request;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;
    private Integer extraFare;

    private LineRequest() {
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

    public Integer getExtraFare() {
        return extraFare;
    }
}