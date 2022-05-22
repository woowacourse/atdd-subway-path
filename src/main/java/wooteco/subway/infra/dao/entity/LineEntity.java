package wooteco.subway.infra.dao.entity;

public class LineEntity {

    private Long id;
    private String name;
    private String color;
    private Long extraFare;

    public LineEntity() {
    }

    public LineEntity(Long id, String name, String color, Long extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public LineEntity(Long id, String name, String color) {
        this(id, name, color, null);
    }

    public LineEntity(String name, String color) {
        this(null, name, color);
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

    public Long getExtraFare() {
        return extraFare;
    }
}
