package wooteco.subway.presentation.dto.request;

import wooteco.subway.domain.station.Station;

import javax.validation.constraints.NotEmpty;

public class StationRequest {

    @NotEmpty
    private String name;

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Station toStation() {
        return new Station(name);
    }
}
