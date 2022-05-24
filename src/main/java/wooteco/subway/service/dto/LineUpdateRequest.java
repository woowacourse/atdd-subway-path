package wooteco.subway.service.dto;

public class LineUpdateRequest {
    private String name;
    private String color;
    private Long extraFare;

    public LineUpdateRequest() {
    }

    public LineUpdateRequest(String name, String color, Long extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getExtraFare() {
        return extraFare;
    }
}
