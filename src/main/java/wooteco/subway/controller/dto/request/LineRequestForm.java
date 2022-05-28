package wooteco.subway.controller.dto.request;

public class LineRequestForm {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;
    private Long extraFare = 0L;

    public LineRequestForm() {
    }

    public LineRequestForm(String name, String color, Long upStationId, Long downStationId,
                           Long distance, Long extraFare) {
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

    public Long getDistance() {
        return distance;
    }

    public Long getExtraFare() {
        return extraFare;
    }
}
