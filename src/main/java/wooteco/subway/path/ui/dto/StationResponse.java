package wooteco.subway.path.ui.dto;

import wooteco.subway.path.application.dto.StationResponseDto;
import wooteco.subway.path.ui.dto.valid.NumberValidation;
import wooteco.subway.path.ui.dto.valid.StringValidation;

public class StationResponse {

    @NumberValidation
    private final Long id;
    @StringValidation
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
