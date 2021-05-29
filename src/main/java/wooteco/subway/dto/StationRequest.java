package wooteco.subway.dto;

import lombok.Getter;
import wooteco.subway.domain.Station;

import javax.validation.constraints.NotNull;

@Getter
public class StationRequest {
    @NotNull
    private String name;

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public Station toStation() {
        return new Station(name);
    }
}
