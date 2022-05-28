package wooteco.subway.infrastructure.jdbc.dao.entity;

public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;
    private final Long extraFare;
    
    public LineEntity(Long id, String name, String color, Long extraFare) {
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

    public Long getExtraFare() {
        return extraFare;
    }
}
