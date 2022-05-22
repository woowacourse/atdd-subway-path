package wooteco.subway.service.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.Line;

public class LineDto {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;
    private final List<StationDto> stations;

    private LineDto(Long id, String name, String color, int extraFare, List<StationDto> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stations;
    }

    public static LineDto from(Line line) {
        return new LineDto(line.getId(), line.getName(), line.getColor(), line.getExtraFare(),
            line.findOrderedStations()
                .stream().map(StationDto::from)
                .collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getExtraFare() {
        return extraFare;
    }

    public List<StationDto> getStations() {
        return List.copyOf(stations);
    }
}
