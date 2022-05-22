package wooteco.subway.controller.dto;

import wooteco.subway.service.dto.StationDto;

public class StationResponse {

    private final Long id;
    private final String name;

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
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
