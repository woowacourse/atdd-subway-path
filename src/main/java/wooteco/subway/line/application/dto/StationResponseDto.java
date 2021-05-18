package wooteco.subway.line.application.dto;

import wooteco.subway.station.domain.Station;

public class StationResponseDto {

    private final Long id;
    private final String name;

    public StationResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponseDto of(Station station) {
        return new StationResponseDto(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
