package wooteco.subway.dto.line;

public class LineUpdateRequest {
    private String name;
    private String color;
    private int extraFare;

    public LineUpdateRequest() {
    }

    public LineUpdateRequest(final String name, final String color, final int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public LineUpdateRequest(String name, String color) {
        this(name, color, 0);
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
