package wooteco.subway.common;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class PathAnd extends Request{

    private final TStation source;
    private final TStation target;

    public PathAnd(TStation source, TStation target) {
        this.source = source;
        this.target = target;
    }

    public ExtractableResponse<Response> 의최단거리를계산한다(int age) {
        return get(String.format("/paths?source=%d&target=%d&age=%s", source.getId(), target.getId(), age));
    }
}
