package wooteco.subway.controller;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.dto.info.LineServiceRequest;
import wooteco.subway.dto.info.LineServiceResponse;
import wooteco.subway.dto.info.LineUpdateRequest;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.dto.response.StationResponse;

public class LineConverter {
    static LineUpdateRequest toInfo(Long id, LineRequest lineRequest) {
        return new LineUpdateRequest(id, lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare());
    }

    static LineServiceRequest toInfo(LineRequest lineRequest) {
        return new LineServiceRequest(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(),
            lineRequest.getDownStationId(), lineRequest.getDistance(), lineRequest.getExtraFare());
    }

    static LineResponse toResponse(LineServiceResponse lineServiceResponse) {
        List<StationResponse> stationResponses = lineServiceResponse.getStationInfos().stream()
            .map(StationConverter::toResponse)
            .collect(Collectors.toList());
        return new LineResponse(lineServiceResponse.getId(), lineServiceResponse.getName(),
            lineServiceResponse.getColor(), lineServiceResponse.getExtraFare(), stationResponses);
    }
}
