package wooteco.subway.dto;

import wooteco.subway.domain.Station;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank
    private String name;

    public StationRequest() {
    }

    public Station toStation() {
        return new Station(this.name);
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
