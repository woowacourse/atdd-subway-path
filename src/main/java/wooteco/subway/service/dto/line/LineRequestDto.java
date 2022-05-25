package wooteco.subway.service.dto.line;

public class LineRequestDto {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int extraFare;

    private LineRequestDto() {
    }

    public LineRequestDto(String name, String color, Long upStationId, Long downStationId, int distance,
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
