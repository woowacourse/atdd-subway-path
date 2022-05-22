package wooteco.subway.domain;

import java.util.Objects;

public class Line {

    private Long id;
    private String name;
    private String color;
    private int extraFare;

    public Line(final Long id, final String name, final String color, final int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line(final String name, final String color, final int extraFare) {
        this(null, name, color, extraFare);
    }

    public Line(final String name, final String color) {
        this(null, name, color, 0);
    }

    public void update(final Line newLine) {
        name = newLine.name;
        color = newLine.color;
        extraFare = newLine.extraFare;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;
        final Line line = (Line) o;
        return extraFare == line.extraFare && Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, extraFare);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
