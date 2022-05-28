package wooteco.subway.acceptance.station;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.acceptance.RequestHandler;
import wooteco.subway.controller.dto.response.StationResponseForm;

public class StationRequestHandler {

    private final RequestHandler requestHandler = new RequestHandler();

    public ExtractableResponse<Response> findStations() {
        return requestHandler.getRequest("/stations");
    }

    public ExtractableResponse<Response> createStation(Map<String, String> params) {
        return requestHandler.postRequest("/stations", params);
    }

    public ExtractableResponse<Response> removeStation(long lineId) {
        return requestHandler.deleteRequest("/stations/" + lineId);
    }

    public long extractId(ExtractableResponse<Response> response) {
        return requestHandler.extractId(response, StationResponseForm.class).getId();
    }

    public List<Long> extractIds(ExtractableResponse<Response> response) {
        return requestHandler.extractIds(response, StationResponseForm.class)
                .stream()
                .map(StationResponseForm::getId)
                .collect(Collectors.toUnmodifiableList());
    }
}
