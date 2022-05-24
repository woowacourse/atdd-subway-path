package wooteco.subway.dto.request;

public class LineCreateRequest {
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;
    private final int extraFare;

    public LineCreateRequest() {
        this(null, null, null, null, 0, 0);
    }

    public LineCreateRequest(String name, String color, Long upStationId, Long downStationId, int distance,
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
