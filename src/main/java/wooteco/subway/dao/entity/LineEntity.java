package wooteco.subway.dao.entity;

import wooteco.subway.domain.Line;

public class LineEntity {

    private Long id;
    private String name;
    private String color;
    private int fare;

    private LineEntity() {
    }

    public LineEntity(Long id, String name, String color, int fare) {
        this(name, color, fare);
        this.id = id;
    }

    public LineEntity(String name, String color, int fare) {
        this(name, color);
        this.fare = fare;
    }

    public LineEntity(String name, String color) {
        this.name = name;
        this.color = color;
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

    public int getFare() {
        return fare;
    }
}
