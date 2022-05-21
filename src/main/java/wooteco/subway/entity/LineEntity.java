package wooteco.subway.entity;

import java.util.Objects;
import wooteco.subway.domain.line.LineInfo;

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

    public LineEntity(String name, String color, int extraFare) {
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
        return extraFare;
    }

    public LineInfo toDomain() {
        return new LineInfo(id, name, color, extraFare);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineEntity that = (LineEntity) o;
        return extraFare == that.extraFare
                && Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, extraFare);
    }

    @Override
    public String toString() {
        return "LineEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", extraFare=" + extraFare +
                '}';
    }
}
