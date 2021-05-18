package wooteco.subway.path.ui.dto;

import wooteco.subway.path.application.dto.StationResponseDto;

public class StationResponse {

    private final Long id;
    private final String name;

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse of(StationResponseDto station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
