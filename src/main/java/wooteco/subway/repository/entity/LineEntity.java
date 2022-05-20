package wooteco.subway.repository.entity;

import wooteco.subway.domain.Line;

public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;

    public LineEntity(Long id, String name, String color, int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public static LineEntity from(Line line) {
        return new LineEntity(line.getId(), line.getName(), line.getColor(), line.getExtraFare());
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
