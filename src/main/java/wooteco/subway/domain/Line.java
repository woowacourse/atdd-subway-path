package wooteco.subway.domain;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;

    public Line(String name, String color, int extraFare) {
        this(0L, name, color, extraFare);
    }

    public Line(Long id, String name, String color, int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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
}
