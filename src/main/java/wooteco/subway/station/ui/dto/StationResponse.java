package wooteco.subway.station.ui.dto;

import wooteco.subway.station.application.dto.StationResponseDto;

import java.beans.ConstructorProperties;

public class StationResponse {

    private final Long id;
    private final String name;

    @ConstructorProperties({"id", "name"})
    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse of(StationResponseDto stationResponseDto) {
        return new StationResponse(
                stationResponseDto.getId(),
                stationResponseDto.getName()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
