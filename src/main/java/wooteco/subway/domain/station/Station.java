package wooteco.subway.domain.station;

public class Station {

    private Long id;
    private final String name;

    public Station(Long id, String name) {
        validate(name);
        this.id = id;
        this.name = name;
    }

    public void validate(String name) {
        if (name == null) {
            throw new IllegalArgumentException("역 이름에는 빈 값이 올 수 없습니다.");
        }
    }

    public Station(String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
