package wooteco.subway.dto;

public class LineEntity {
    private final long id;
    private final String name;
    private final String color;
    private final int extraFare;

    public LineEntity(long id, String name, String color, int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public long getId() {
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
