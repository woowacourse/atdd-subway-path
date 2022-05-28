package wooteco.subway.domain;

import java.util.Objects;

public class Station {

    private final Long id;
    private final String name;

    public Station(Long id, String name) {
        validate(name);
        this.id = id;
        this.name = name;
    }

    private void validate(String name) {
        if (name == null ||name.isBlank()) {
            throw new IllegalArgumentException("역 이름을 입력해주세요.");
        }
    }

    public Station(String name) {
        this(null, name);
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public boolean hasId(Long stationId) {
        return id.equals(stationId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
