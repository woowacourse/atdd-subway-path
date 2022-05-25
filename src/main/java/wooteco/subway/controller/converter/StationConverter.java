package wooteco.subway.controller.converter;

import wooteco.subway.controller.dto.request.StationRequest;
import wooteco.subway.controller.dto.response.StationResponse;
import wooteco.subway.service.dto.StationDto;

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
