package wooteco.subway.acceptance.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.acceptance.RequestHandler;

public class PathRequestHandler {

    private final RequestHandler requestHandler = new RequestHandler();

    public ExtractableResponse<Response> findPath(long source, long target) {
        return requestHandler.getRequest(String.format("/paths?source=%d&target=%d", source, target));
    }
}
