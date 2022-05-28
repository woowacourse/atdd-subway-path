package wooteco.subway.service.dto;

import wooteco.subway.domain.Station;

public class StationServiceResponse {

    private final long id;
    private final String name;

    public StationServiceResponse(Station station) {
        this.id = station.getId();
        this.name = station.getName();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
