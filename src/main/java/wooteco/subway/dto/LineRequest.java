package wooteco.subway.dto;

public class LineRequest {

    private String name;
    private String color;
    private long upStationId;
    private long downStationId;
    private int distance;
    private int extraFare;

    public LineRequest() {
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

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
