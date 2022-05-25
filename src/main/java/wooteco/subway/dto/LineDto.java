package wooteco.subway.dto;

import wooteco.subway.domain.Line;

public class LineDto {

    private Long id;
    private String name;
    private String color;
    private int extraFare;

    private LineDto() {
    }

    public LineDto(String name, String color) {
        this.name = name;
        this.color = color;
        this.extraFare = 0;
    }

    public LineDto(Long id, String name, String color, int extraFare) {
        this(name, color);
        this.id = id;
        this.extraFare = extraFare;
    }

    public static LineDto from(Line line) {
        return new LineDto(line.getId(), line.getName(), line.getColor(), line.getExtraFare());
    }

    public Long getId() {
        return id;
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
