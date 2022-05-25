package wooteco.subway.controller.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import wooteco.subway.service.dto.response.LineResponse;
import wooteco.subway.service.dto.response.PathResponse;
import wooteco.subway.service.dto.response.StationResponse;

@Component
public class ResponseFormAssembler {

    public LineResponseForm lineResponseForm(LineResponse lineResponse, List<StationResponse> stationResponses) {
        return new LineResponseForm(lineResponse.getId(),
                lineResponse.getName(), lineResponse.getColor(),
                stationResponseForms(stationResponses));
    }

    public List<StationResponseForm> stationResponseForms(List<StationResponse> stationResponses) {
        return stationResponses.stream()
                .map(this::stationResponseForm)
                .collect(Collectors.toUnmodifiableList());
    }

    public StationResponseForm stationResponseForm(StationResponse stationResponse) {
        return new StationResponseForm(stationResponse.getId(), stationResponse.getName());
    }

    public PathResponseForm pathResponseForm(PathResponse pathResponse, List<StationResponse> stationResponses) {
        return new PathResponseForm(stationResponseForms(stationResponses),
                pathResponse.getDistance(), pathResponse.getFare());
    }
}
