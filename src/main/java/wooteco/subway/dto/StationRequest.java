package wooteco.subway.dto;

import lombok.Getter;
import wooteco.subway.domain.Station;

@Getter
public class StationRequest {
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
