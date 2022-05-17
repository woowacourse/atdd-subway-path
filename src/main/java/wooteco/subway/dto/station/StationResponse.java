package wooteco.subway.dto.station;

import wooteco.subway.domain.station.Station;

public class StationResponse {

    private final Long id;
    private final String name;

    public StationResponse(Station station) {
        this(station.getId(), station.getName());
    }

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
