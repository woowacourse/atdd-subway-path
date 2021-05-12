package wooteco.subway.controller.dto.request;

import wooteco.subway.domain.Station;

public class StationRequestDto {
    private String name;

    public StationRequestDto() {
    }

    public StationRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Station toStation() {
        return new Station(name);
    }
}
