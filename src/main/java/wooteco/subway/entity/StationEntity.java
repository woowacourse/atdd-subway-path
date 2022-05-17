package wooteco.subway.entity;

import java.util.Objects;
import wooteco.subway.domain.station.Station;

public class StationEntity {

    private final Long id;
    private final String name;

    public StationEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StationEntity(String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Station toDomain() {
        return new Station(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationEntity that = (StationEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StationEntity{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
