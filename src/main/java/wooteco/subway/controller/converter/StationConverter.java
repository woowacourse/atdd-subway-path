package wooteco.subway.controller.converter;

import wooteco.subway.dto.info.StationDto;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.StationResponse;

public class StationConverter {
    public static StationDto toInfo(StationRequest stationRequest) {
        return new StationDto(stationRequest.getName());
    }

    public static StationDto toInfo(Long id, StationRequest stationRequest) {
        return new StationDto(id, stationRequest.getName());
    }

    public static StationResponse toResponse(StationDto stationDto) {
        return new StationResponse(stationDto.getId(), stationDto.getName());
    }
}
