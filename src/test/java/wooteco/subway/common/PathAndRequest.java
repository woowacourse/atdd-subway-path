package wooteco.subway.common;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class PathAndRequest extends Request {

    private final TStation source;
    private final TStation target;

    public PathAndRequest(TStation source, TStation target) {
        this.source = source;
        this.target = target;
    }

    public ExtractableResponse<Response> 의최단거리를계산한다(int age, int status) {
        ExtractableResponse<Response> response = 최단거리를계산한다(age);
        assertThat(response.statusCode()).isEqualTo(status);
        return response;
    }

    public ExtractableResponse<Response> 최단거리를계산한다(int age) {
        return get(
                String.format("/paths?source=%d&target=%d&age=%s",
                        source.getId(),
                        target.getId(),
                        age)
        );
    }
}
