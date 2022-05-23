package wooteco.subway.controller.converter;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.dto.controller.request.LineRequest;
import wooteco.subway.dto.controller.response.LineResponse;
import wooteco.subway.dto.controller.response.StationResponse;
import wooteco.subway.dto.service.request.LineServiceRequest;
import wooteco.subway.dto.service.request.LineUpdateRequest;
import wooteco.subway.dto.service.response.LineServiceResponse;

public class LineConverter {
    public static LineUpdateRequest toInfo(Long id, LineRequest lineRequest) {
        return new LineUpdateRequest(id, lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare());
    }

    public static LineServiceRequest toInfo(LineRequest lineRequest) {
        return new LineServiceRequest(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(),
            lineRequest.getDownStationId(), lineRequest.getDistance(), lineRequest.getExtraFare());
    }

    public static LineResponse toResponse(LineServiceResponse lineServiceResponse) {
        List<StationResponse> stationResponses = lineServiceResponse.getStationInfos().stream()
            .map(StationConverter::toResponse)
            .collect(Collectors.toList());
        return new LineResponse(lineServiceResponse.getId(), lineServiceResponse.getName(),
            lineServiceResponse.getColor(), lineServiceResponse.getExtraFare(), stationResponses);
    }
}
