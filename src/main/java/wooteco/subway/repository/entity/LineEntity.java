package wooteco.subway.repository.entity;

import wooteco.subway.domain.Line;

public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;
    private final Long extraFare;

    public LineEntity(Long id, String name, String color, Long extraFare) {
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

    public Long getExtraFare() {
        return extraFare;
    }
}
