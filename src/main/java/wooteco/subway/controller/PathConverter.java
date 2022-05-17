package wooteco.subway.controller;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.dto.info.PathServiceRequest;
import wooteco.subway.dto.info.PathServiceResponse;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

public class PathConverter {
    static PathServiceRequest toInfo(Long source, Long target, int age) {
        return new PathServiceRequest(source, target, age);
    }

    static PathResponse toResponse(PathServiceResponse pathServiceResponse) {
        List<StationResponse> stationResponses = pathServiceResponse.getStations()
            .stream()
            .map(stationDto -> new StationResponse(stationDto.getId(), stationDto.getName()))
            .collect(Collectors.toList());
        return new PathResponse(stationResponses, pathServiceResponse.getDistance());
    }
}
