package wooteco.subway.domain.line;

import java.util.Objects;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final LineExtraFare extraFare;

    public Line(Long id, String name, String color, LineExtraFare extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line(Long id, String name, String color, int extraFare) {
        this(id, name, color, new LineExtraFare(extraFare));
    }

    public Line(String name, String color, int extraFare) {
        this(null, name, color, extraFare);
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
        return extraFare.getValue();
    }

    public LineExtraFare getLineExtraFare() {
        return extraFare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id)
                && Objects.equals(name, line.name)
                && Objects.equals(color, line.color)
                && Objects.equals(extraFare, line.extraFare);
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
                ", extraFare=" + extraFare +
                '}';
    }
}
