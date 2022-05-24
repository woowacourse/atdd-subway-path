package wooteco.subway.service.dto;

public class LineModificationServiceRequest {

    private String name;
    private String color;
    private int extraFare;

    public LineModificationServiceRequest(String name, String color, int extraFare) {
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

    public int getExtraFare() {
        return extraFare;
    }
}
