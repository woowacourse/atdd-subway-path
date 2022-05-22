package wooteco.subway.domain;

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
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
