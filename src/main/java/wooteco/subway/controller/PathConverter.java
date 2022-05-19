package wooteco.subway.controller;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.dto.controller.response.PathResponse;
import wooteco.subway.dto.controller.response.StationResponse;
import wooteco.subway.dto.service.PathServiceRequest;
import wooteco.subway.dto.service.PathServiceResponse;

public class PathConverter {
    static PathServiceRequest toServiceRequest(Long source, Long target, int age) {
        return new PathServiceRequest(source, target, age);
    }

    static PathResponse toResponse(PathServiceResponse pathServiceResponse) {
        List<StationResponse> stationResponses = pathServiceResponse.getStations()
            .stream()
            .map(stationDto -> new StationResponse(stationDto.getId(), stationDto.getName()))
            .collect(Collectors.toList());
        return new PathResponse(stationResponses, pathServiceResponse.getDistance(), pathServiceResponse.getFare());
    }
}
