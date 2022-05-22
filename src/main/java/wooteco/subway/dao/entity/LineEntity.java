package wooteco.subway.dao.entity;

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

    public LineEntity(Long id, String name, String color) {
        this(id,name,color,0);
    }

    public LineEntity(String name, String color) {
        this(null, name, color, 0);
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
