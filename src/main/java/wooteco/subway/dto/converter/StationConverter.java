package wooteco.subway.dto.converter;

import wooteco.subway.dto.controller.request.StationRequest;
import wooteco.subway.dto.controller.response.StationResponse;
import wooteco.subway.dto.service.StationDto;

public class StationConverter {
    public static StationDto toServiceRequest(StationRequest stationRequest) {
        return new StationDto(stationRequest.getName());
    }

    public static StationResponse toResponse(StationDto stationDto) {
        return new StationResponse(stationDto.getId(), stationDto.getName());
    }
}
