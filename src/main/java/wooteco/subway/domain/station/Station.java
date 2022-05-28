package wooteco.subway.domain.station;

import java.util.Objects;

import wooteco.subway.domain.Id;

public class Station {

    private final Id id;
    private final StationName name;

    public Station(long id, String name) {
        this.id = new Id(id);
        this.name = new StationName(name);
    }

    public Station(String name) {
        this.id = Id.temporary();
        this.name = new StationName(name);
    }

    public long getId() {
        return id.getId();
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
        return Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
