package wooteco.subway.domain.line;

import java.util.Objects;

public class LineInfo {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;

    public LineInfo(Long id, String name, String color, int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public LineInfo(String name, String color, int extraFare) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineInfo lineInfo = (LineInfo) o;
        return extraFare == lineInfo.extraFare
                && Objects.equals(id, lineInfo.id)
                && Objects.equals(name, lineInfo.name)
                && Objects.equals(color, lineInfo.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, extraFare);
    }

    @Override
    public String toString() {
        return "LineInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", extraFare=" + extraFare +
                '}';
    }
}
