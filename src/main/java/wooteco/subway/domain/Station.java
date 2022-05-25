package wooteco.subway.domain;

import java.util.Objects;
import wooteco.subway.domain.vo.StationName;

public class Station {
    private Long id;
    private StationName name;

    public Station(Long id, StationName name) {
        this.id = id;
        this.name = name;
    }

    public Station(StationName name) {
        this.name = name;
    }

    public boolean isSameId(Long id) {
        return Objects.equals(this.id, id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
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
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

