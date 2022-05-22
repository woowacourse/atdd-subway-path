package wooteco.subway.dto;

public class LineRequest {
    private String name;
    private String color;
    private Integer extraFare;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    private LineRequest() {
    }

    public LineRequest(String name, String color, Integer extraFare, Long upStationId, Long downStationId,
                       Integer distance) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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

    public Integer getExtraFare() {
        return extraFare;
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

    @Override
    public String toString() {
        return "LineRequest{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", extraFare=" + extraFare +
                ", upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", distance=" + distance +
                '}';
    }
}
