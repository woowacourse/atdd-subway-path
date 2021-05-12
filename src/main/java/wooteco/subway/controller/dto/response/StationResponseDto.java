package wooteco.subway.controller.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Station;

public class StationResponseDto {
    private Long id;
    private String name;

    public static StationResponseDto of(Station station) {
        return new StationResponseDto(station.getId(), station.getName());
    }

    public StationResponseDto() {
    }

    public StationResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<StationResponseDto> listOf(List<Station> stations) {
        return stations.stream()
            .map(StationResponseDto::of)
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
