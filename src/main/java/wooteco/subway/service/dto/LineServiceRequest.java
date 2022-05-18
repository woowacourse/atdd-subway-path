package wooteco.subway.service.dto;

public class LineServiceRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public LineServiceRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public LineServiceRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(name, color);
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
