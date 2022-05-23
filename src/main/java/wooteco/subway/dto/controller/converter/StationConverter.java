package wooteco.subway.dto.controller.converter;

import wooteco.subway.dto.controller.request.StationRequest;
import wooteco.subway.dto.controller.response.StationResponse;
import wooteco.subway.dto.service.StationDto;

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
