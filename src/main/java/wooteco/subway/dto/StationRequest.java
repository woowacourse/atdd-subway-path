package wooteco.subway.dto;

import wooteco.subway.domain.station.Station;

import javax.validation.constraints.NotNull;

public class StationRequest {

    @NotNull
    private String name;

    private StationRequest() {
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
