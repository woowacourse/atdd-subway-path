package wooteco.subway.domain;

import java.util.Objects;

public class Line {

    private final Long id;
    private final Name name;
    private final String color;
    private final int extraFare;
    private final Sections sections;

    public Line(final Long id, final Name name, final String color, final int extraFare, final Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    public Line(final Long id, final Name name, final String color) {
        this(id, name, color, 0, null);
    }

    public Line(final String name, final String color) {
        this(null, new Name(name), color, 0, null);
    }

    public Line(final Long id, final String name, final String color) {
        this(id, new Name(name), color, 0, null);
    }

    public Line(final String name, final String color, final int extraFare) {
        this(null, new Name(name), color, extraFare, null);
    }

    public Line(final long id, final Name name, final String color, final int extraFare) {
        this(id, name, color, extraFare, null);
    }

    public Line addSections(final Sections sections) {
        return new Line(id, name, color, 0, sections);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getColor() {
        return color;
    }

    public int getExtraFare() {
        return extraFare;
    }

    public Sections getSections() {
        return sections;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Line line = (Line) o;
        return Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name=" + name +
                ", color='" + color + '\'' +
                ", extraFare=" + extraFare +
                ", sections=" + sections +
                '}';
    }
}
