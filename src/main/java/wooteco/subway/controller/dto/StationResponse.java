package wooteco.subway.controller.dto;

import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.StationDto;

public class StationResponse {
    private Long id;
    private String name;

    public StationResponse() {
    }

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse from(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static StationResponse from(StationDto stationDto) {
        return new StationResponse(stationDto.getId(), stationDto.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
