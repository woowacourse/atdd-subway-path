package wooteco.subway.ui.dto;

import wooteco.subway.domain.line.Line;

public class LineRequest {

    private String name;
    private String color;
    private int extraFare;

    private LineRequest() {
    }

    public LineRequest(String name, String color, int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line toEntity(Long id) {
        return new Line(id, name, color, extraFare);
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

    @Override
    public String toString() {
        return "LineRequest{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", extraFare=" + extraFare +
                '}';
    }
}
