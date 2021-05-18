package wooteco.subway.station.ui.dto;

import wooteco.subway.station.application.dto.StationResponseDto;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

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
