package wooteco.subway.controller;

import wooteco.subway.dto.controller.request.StationRequest;
import wooteco.subway.dto.controller.response.StationResponse;
import wooteco.subway.dto.service.StationDto;

public class StationConverter {
    static StationDto toServiceRequest(StationRequest stationRequest) {
        return new StationDto(stationRequest.getName());
    }

    static StationResponse toResponse(StationDto stationDto) {
        return new StationResponse(stationDto.getId(), stationDto.getName());
    }
}
