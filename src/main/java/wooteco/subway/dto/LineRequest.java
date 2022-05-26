package wooteco.subway.dto;

public class LineRequest {

    private String name;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int extraFare;
    private String color;

    public LineRequest(String name, Long upStationId, Long downStationId, int distance, int extraFare, String color) {
        this.name = name;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
        this.color = color;
    }

    public LineRequest() {

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
