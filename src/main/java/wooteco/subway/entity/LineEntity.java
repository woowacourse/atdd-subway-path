package wooteco.subway.entity;

import wooteco.subway.domain.line.Line;

public class LineEntity implements Entity {

    private final Long id;
    private final String name;
    private final String color;
    private final Integer extraFare;

    public LineEntity(Long id, String name, String color, Integer extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public LineEntity(String name, String color, Integer extraFare) {
        this(null, name, color, extraFare);
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

    public Integer getExtraFare() {
        return extraFare;
    }
}
