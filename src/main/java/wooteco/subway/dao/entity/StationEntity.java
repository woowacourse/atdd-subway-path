package wooteco.subway.dao.entity;

public class StationEntity {

    private final Long id;
    private final String name;

    public StationEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StationEntity(String name) {
        this(0L, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
