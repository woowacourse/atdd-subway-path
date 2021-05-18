package wooteco.subway.station.ui.dto;

import wooteco.subway.station.application.dto.StationResponseDto;

public class StationResponse {
    private Long id;
    private String name;

    public StationResponse() {
    }

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static StationResponse of(StationResponseDto stationResponseDto) {
        return new StationResponse(
                stationResponseDto.getId(),
                stationResponseDto.getName()
        );
    }

}
