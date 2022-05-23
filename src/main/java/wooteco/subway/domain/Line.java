package wooteco.subway.domain;

public class Line {

    private long id;
    private String name;
    private String color;
    private int extraFare;

    public Line(String name, String color, int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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

    public boolean isSameId(long id) {
        return this.id == id;
    }

    public boolean isSameExtraFare(int extraFare) {
        return this.extraFare == extraFare;
    }
}
