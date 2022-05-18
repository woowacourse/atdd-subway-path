package wooteco.subway.dto;

import org.jetbrains.annotations.NotNull;
import wooteco.subway.domain.station.Station;

public class StationRequest {

    @NotNull
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
