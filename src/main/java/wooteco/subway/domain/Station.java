package wooteco.subway.domain;

import java.util.Objects;

public class Station {
    private Long id;
    private String name;

    public Station() {
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isSameId(Long id) {
        return this.id.equals(id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
        return id != null ? id.hashCode() : 0;
    }
}

