package wooteco.subway.domain.station;

public class Station {
    private Long id;
    private final String name;

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station(String name) {
        this(null, name);
    }

    public boolean isSameId(Long id) {
        return id.equals(this.id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
