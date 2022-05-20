package wooteco.subway.common;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

public class PathAndRequest extends Request{

    private final TStation source;
    private final TStation target;

    public PathAndRequest(TStation source, TStation target) {
        this.source = source;
        this.target = target;
    }

    public ExtractableResponse<Response> 의최단거리를계산한다(int age) {
        ExtractableResponse<Response> response = get(
                String.format("/paths?source=%d&target=%d&age=%s",
                        source.getId(),
                        target.getId(),
                        age)
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }
}
