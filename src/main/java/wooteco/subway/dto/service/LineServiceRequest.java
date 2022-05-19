package wooteco.subway.dto.service;

public class LineServiceRequest {
    private final String name;
    private final String color;
    private final long upStationId;
    private final long downStationId;
    private final int distance;
    private final int extraFare;

    public LineServiceRequest(String name, String color, long upStationId, long downStationId, int distance,
        int extraFare) {
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
