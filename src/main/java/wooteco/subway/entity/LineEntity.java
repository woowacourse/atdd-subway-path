package wooteco.subway.entity;

import wooteco.subway.domain.line.Line;

public class LineEntity implements Entity {

    private final Long id;
    private final String name;
    private final String color;

    public LineEntity(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LineEntity(String name, String color) {
        this(null, name, color);
    }

    public static LineEntity from(Line line) {
        return new LineEntity(line.getId(), line.getName(), line.getColor());
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
}