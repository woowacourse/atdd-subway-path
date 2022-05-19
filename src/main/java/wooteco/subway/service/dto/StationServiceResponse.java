package wooteco.subway.service.dto;

import wooteco.subway.domain.Station;

public class StationServiceResponse {

    private final Long id;
    private final String name;

    public StationServiceResponse(Station station) {
        this.id = station.getId();
        this.name = station.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
