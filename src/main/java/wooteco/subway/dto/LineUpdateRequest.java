package wooteco.subway.dto;

public class LineUpdateRequest {

    private String name;
    private String color;
    private int extraFare;

    private LineUpdateRequest() {
    }

    public LineUpdateRequest(String name, String color) {
        this(name, color, 0);
    }

    public LineUpdateRequest(String name, String color, int extraFare) {
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
}
