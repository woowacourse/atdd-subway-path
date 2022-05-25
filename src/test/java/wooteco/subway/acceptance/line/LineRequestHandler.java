package wooteco.subway.acceptance.line;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.acceptance.RequestHandler;
import wooteco.subway.controller.dto.response.LineResponseForm;

public class LineRequestHandler {

    private final RequestHandler requestHandler = new RequestHandler();

    public ExtractableResponse<Response> findLines() {
        return requestHandler.getRequest("/lines");
    }

    public ExtractableResponse<Response> findLine(long lineId) {
        return requestHandler.getRequest("/lines/" + lineId);
    }

    public ExtractableResponse<Response> createLine(Map<String, String> params) {
        return requestHandler.postRequest("/lines", params);
    }

    public ExtractableResponse<Response> updateLine(long lineId, Map<String, String> params) {
        return requestHandler.putRequest("/lines/" + lineId, params);
    }

    public ExtractableResponse<Response> appendSection(long lineId, Map<String, String> params) {
        return requestHandler.postRequest("/lines/" + lineId + "/sections", params);
    }

    public ExtractableResponse<Response> removeStation(long lineId, long stationId) {
        return requestHandler.deleteRequest("/lines/" + lineId + "/sections/?stationId=" + stationId);
    }

    public ExtractableResponse<Response> removeLine(long lineId) {
        return requestHandler.deleteRequest("/lines/" + lineId);
    }

    public long extractId(ExtractableResponse<Response> response) {
        return requestHandler.extractId(response, LineResponseForm.class).getId();
    }

    public List<Long> extractIds(ExtractableResponse<Response> response) {
        return requestHandler.extractIds(response, LineResponseForm.class)
                .stream()
                .map(LineResponseForm::getId)
                .collect(Collectors.toUnmodifiableList());
    }
}
