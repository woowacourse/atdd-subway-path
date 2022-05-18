package wooteco.subway.ui.dto;

import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.StationServiceResponse;

public class StationResponse {
    private Long id;
    private String name;

    public StationResponse() {
    }

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StationResponse(StationServiceResponse stationServiceResponse) {
        this(stationServiceResponse.getId(), stationServiceResponse.getName());
    }

    public StationResponse(Station station) {
        this(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
