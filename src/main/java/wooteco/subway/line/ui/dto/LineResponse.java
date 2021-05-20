package wooteco.subway.line.ui.dto;

import wooteco.subway.line.application.dto.LineResponseDto;
import wooteco.subway.line.ui.dto.valid.NumberValidation;
import wooteco.subway.line.ui.dto.valid.StringValidation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class LineResponse {

    @NumberValidation
    private final Long id;
    @StringValidation
    private final String name;
    @StringValidation
    private final String color;
    @NotNull
    @Size(min = 2)
    private final List<StationResponse> stations;

    @ConstructorProperties({"id", "name", "color", "stations"})
    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse of(LineResponseDto lineResponseDto) {
        return new LineResponse(
                lineResponseDto.getId(),
                lineResponseDto.getName(),
                lineResponseDto.getColor(),
                getStationResponses(lineResponseDto)
        );
    }

    private static List<StationResponse> getStationResponses(LineResponseDto lineResponseDto) {
        return lineResponseDto.getStations().stream()
                .map(StationResponse::of)
                .collect(toList());
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

    public List<StationResponse> getStations() {
        return stations;
    }

}