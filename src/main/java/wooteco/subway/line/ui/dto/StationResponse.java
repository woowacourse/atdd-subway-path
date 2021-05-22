package wooteco.subway.line.ui.dto;

import wooteco.subway.line.application.dto.StationResponseDto;
import wooteco.subway.line.ui.dto.valid.NumberValidation;
import wooteco.subway.line.ui.dto.valid.StringValidation;

import java.beans.ConstructorProperties;

public class StationResponse {

    @NumberValidation
    private final Long id;
    @StringValidation
    private final String name;

    @ConstructorProperties({"id", "name"})
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
