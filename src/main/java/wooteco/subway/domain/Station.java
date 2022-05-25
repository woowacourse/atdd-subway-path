package wooteco.subway.domain;

import java.util.Objects;
import wooteco.subway.domain.vo.StationId;
import wooteco.subway.domain.vo.StationName;

public class Station {
    private StationId id;
    private StationName name;

    public Station(StationId id, StationName name) {
        this.id = id;
        this.name = name;
    }

    public Station(StationName name) {
        this.name = name;
    }

    public boolean isSameId(StationId id) {
        return Objects.equals(this.id, id);
    }

    public Long getId() {
        return id.getStationId();
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

